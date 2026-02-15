package com.secureexam.backend.question.service;

import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.repository.ExamRepository;
import com.secureexam.backend.question.dto.QuestionResponse;
import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    public QuestionService(
            QuestionRepository questionRepository,
            ExamRepository examRepository) {
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
    }

    public Question addQuestion(Long examId, Question question) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        question.setExam(exam);
        return questionRepository.save(question);
    }

    public List<QuestionResponse> getShuffledQuestions(Long examId) {

        List<Question> questions = questionRepository.findByExamId(examId);

        java.util.Collections.shuffle(questions);

        return questions.stream()
                .map(q -> {
                    List<String> options = new java.util.ArrayList<>();
                    options.add(q.getOptionA());
                    options.add(q.getOptionB());
                    options.add(q.getOptionC());
                    options.add(q.getOptionC());
                    options.add(q.getOptionD());
                    java.util.Collections.shuffle(options);

                    return new QuestionResponse(
                            q.getId(),
                            q.getQuestionText(),
                            options);
                })
                .toList();
    }
}