package com.secureexam.backend.attempt.service;

import com.secureexam.backend.attempt.dto.AnswerRequest;
import com.secureexam.backend.attempt.entity.ExamAttempt;
import com.secureexam.backend.attempt.entity.StudentAnswer;
import com.secureexam.backend.attempt.repository.ExamAttemptRepository;
import com.secureexam.backend.attempt.repository.StudentAnswerRepository;
import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.exam.repository.ExamRepository;
import com.secureexam.backend.question.repository.QuestionRepository;
import com.secureexam.backend.security.entity.SuspiciousActivity;
import com.secureexam.backend.security.repository.SuspiciousActivityRepository;
import com.secureexam.backend.user.User;
import com.secureexam.backend.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamSubmissionService {

        private final ExamAttemptRepository attemptRepo;
        private final StudentAnswerRepository answerRepo;
        private final ExamRepository examRepo;
        private final QuestionRepository questionRepo;
        private final UserRepository userRepo;
        private final SuspiciousActivityRepository suspiciousRepo;

        public ExamSubmissionService(
                        ExamAttemptRepository attemptRepo,
                        StudentAnswerRepository answerRepo,
                        ExamRepository examRepo,
                        QuestionRepository questionRepo,
                        UserRepository userRepo,
                        SuspiciousActivityRepository suspiciousRepo) {

                this.attemptRepo = attemptRepo;
                this.answerRepo = answerRepo;
                this.examRepo = examRepo;
                this.questionRepo = questionRepo;
                this.userRepo = userRepo;
                this.suspiciousRepo = suspiciousRepo;
        }

        @Transactional
        public com.secureexam.backend.attempt.dto.SubmissionResponse submitExam(Long examId, Long userId,
                        List<AnswerRequest> answers) {

                Exam exam = examRepo.findById(examId)
                                .orElseThrow(() -> new RuntimeException("Exam not found"));

                // Prevent late submission + log suspicious activity
                if (exam.getEndTime() != null &&
                                LocalDateTime.now().isAfter(exam.getEndTime())) {

                        SuspiciousActivity log = new SuspiciousActivity();
                        log.setUserId(userId);
                        log.setExamId(examId);
                        log.setActivityType("LATE_SUBMISSION");
                        log.setDetails("Attempted after exam end time");
                        log.setTimestamp(LocalDateTime.now());

                        suspiciousRepo.save(log);

                        throw new RuntimeException("Exam time is over.");
                }

                // Prevent early submission
                if (exam.getStartTime() != null &&
                                LocalDateTime.now().isBefore(exam.getStartTime())) {
                        throw new RuntimeException("Exam not started yet.");
                }

                User student = userRepo.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Get existing attempt
                ExamAttempt attempt = attemptRepo.findByExamAndStudent(exam, student)
                                .orElseThrow(() -> new RuntimeException(
                                                "Exam attempt not found. Please start the exam first."));

                // Check if already submitted
                if (attempt.getSubmittedAt() != null) {
                        throw new RuntimeException("Exam already submitted.");
                }

                // attempt.setStartedAt(LocalDateTime.now()); // Started at should be set in
                // startAttempt

                // attempt = attemptRepo.save(attempt); // Will save at the end

                int score = 0;

                for (AnswerRequest req : answers) {

                        Question question = questionRepo.findById(req.questionId())
                                        .orElseThrow(() -> new RuntimeException("Question not found"));

                        boolean correct = question.getCorrectAnswer()
                                        .equalsIgnoreCase(req.answer());

                        if (correct)
                                score++;

                        StudentAnswer answer = new StudentAnswer();
                        answer.setExamAttempt(attempt);
                        answer.setQuestion(question);
                        answer.setSelectedAnswer(req.answer());
                        answer.setCorrect(correct);

                        answerRepo.save(answer);
                }

                attempt.setScore(score);
                attempt.setSubmittedAt(LocalDateTime.now());

                attempt = attemptRepo.save(attempt); // Ensure attempt is updated

                return new com.secureexam.backend.attempt.dto.SubmissionResponse(
                                attempt.getScore(),
                                attempt.getSuspiciousScore(),
                                attempt.isFlagged());
        }

        public com.secureexam.backend.attempt.dto.AttemptReviewDTO getAttemptReview(Long attemptId) {
                ExamAttempt attempt = attemptRepo.findById(attemptId)
                                .orElseThrow(() -> new RuntimeException("Attempt not found"));

                List<StudentAnswer> studentAnswers = answerRepo.findByExamAttempt(attempt);
                List<SuspiciousActivity> logs = suspiciousRepo.findByUserIdAndExamId(attempt.getStudent().getId(),
                                attempt.getExam().getId());

                List<com.secureexam.backend.attempt.dto.QuestionReviewDTO> questionReviews = studentAnswers.stream()
                                .map(ans -> {
                                        Question q = ans.getQuestion();
                                        return new com.secureexam.backend.attempt.dto.QuestionReviewDTO(
                                                        q.getQuestionText(),
                                                        ans.getSelectedAnswer(),
                                                        q.getCorrectAnswer(),
                                                        ans.isCorrect(),
                                                        List.of(q.getOptionA(), q.getOptionB(), q.getOptionC(),
                                                                        q.getOptionD()));
                                }).collect(java.util.stream.Collectors.toList());

                List<String> logMessages = logs.stream()
                                .map(log -> log.getActivityType() + ": " + log.getDetails() + " (" + log.getTimestamp()
                                                + ")")
                                .collect(java.util.stream.Collectors.toList());

                return new com.secureexam.backend.attempt.dto.AttemptReviewDTO(
                                attempt.getId(),
                                attempt.getStudent().getName(), // Assuming User has getName()
                                attempt.getExam().getTitle(),
                                attempt.getScore(),
                                studentAnswers.size(),
                                questionReviews,
                                logMessages);
        }

        public com.secureexam.backend.attempt.dto.ExamStartResponse startAttempt(Long examId, Long userId) {
                Exam exam = examRepo.findById(examId)
                                .orElseThrow(() -> new RuntimeException("Exam not found"));

                User student = userRepo.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (attemptRepo.existsByExamAndStudent(exam, student)) {
                        throw new RuntimeException("Exam already attempted.");
                }

                ExamAttempt attempt = new ExamAttempt();
                attempt.setExam(exam);
                attempt.setStudent(student);
                attempt.setStartedAt(LocalDateTime.now());

                attempt = attemptRepo.save(attempt);

                List<Question> questions = questionRepo.findByExamId(examId);

                List<com.secureexam.backend.question.dto.QuestionResponse> questionResponses = questions.stream()
                                .map(q -> new com.secureexam.backend.question.dto.QuestionResponse(
                                                q.getId(),
                                                q.getQuestionText(),
                                                List.of(q.getOptionA(), q.getOptionB(), q.getOptionC(),
                                                                q.getOptionD())))
                                .collect(Collectors.toList());

                return new com.secureexam.backend.attempt.dto.ExamStartResponse(
                                attempt.getId(),
                                exam.getId(),
                                exam.getTitle(),
                                exam.getDurationMinutes(),
                                questionResponses);
        }

        public List<com.secureexam.backend.attempt.dto.AttemptSummaryDTO> getAllAttempts() {
                return attemptRepo.findAll().stream()
                                .map(attempt -> new com.secureexam.backend.attempt.dto.AttemptSummaryDTO(
                                                attempt.getId(),
                                                new com.secureexam.backend.attempt.dto.AttemptSummaryDTO.StudentSummary(
                                                                attempt.getStudent().getEmail()),
                                                new com.secureexam.backend.attempt.dto.AttemptSummaryDTO.ExamSummary(
                                                                attempt.getExam().getTitle()),
                                                attempt.getScore(),
                                                attempt.getSuspiciousScore(),
                                                attempt.isFlagged()))
                                .collect(Collectors.toList());
        }
}
