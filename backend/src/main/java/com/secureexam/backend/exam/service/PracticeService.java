package com.secureexam.backend.exam.service;

import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.exam.repository.ExamRepository;
import com.secureexam.backend.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PracticeService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final OpenAIService openAIService;

    public PracticeService(ExamRepository examRepository, QuestionRepository questionRepository,
            OpenAIService openAIService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.openAIService = openAIService;
    }

    public void generatePracticeContent() {
        String[] topics = { "Java", "OOPs", "DBMS", "OS", "Computer Networks", "Cybersecurity" };

        for (String topic : topics) {
            String examTitle = "Practice: " + topic;

            // Check if exam exists
            Optional<Exam> existingExam = examRepository.findAll().stream()
                    .filter(e -> e.getTitle().equals(examTitle) && "PRACTICE".equals(e.getCategory()))
                    .findFirst();

            Exam exam;
            if (existingExam.isPresent()) {
                exam = existingExam.get();
                // Optionally check if we need more questions
                long questionCount = questionRepository.findByExamId(exam.getId()).size();
                if (questionCount >= 50) {
                    continue; // Already enough questions
                }
            } else {
                exam = new Exam();
                exam.setTitle(examTitle);
                exam.setDescription("Practice interview questions for " + topic);
                exam.setDurationMinutes(0); // Unlimited time
                exam.setCategory("PRACTICE");
                exam = examRepository.save(exam);
            }

            // Generate questions in batches
            // We want total 50. If we have 0, we need 50.
            int currentCount = questionRepository.findByExamId(exam.getId()).size();
            int needed = 50 - currentCount;

            // Generate in batches of 10 to be safe with timeouts/tokens
            while (needed > 0) {
                int batchSize = Math.min(needed, 10);
                List<Question> newQuestions = openAIService.generateQuestions(topic, batchSize);

                if (newQuestions.isEmpty()) {
                    System.out.println("Failed to generate questions for " + topic);
                    break;
                }

                for (Question q : newQuestions) {
                    q.setExam(exam);
                }
                questionRepository.saveAll(newQuestions);

                needed -= newQuestions.size();
                try {
                    Thread.sleep(1000); // Rate limit protection
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public List<Exam> getPracticeExams() {
        return examRepository.findAll().stream()
                .filter(e -> "PRACTICE".equals(e.getCategory()))
                .toList();
    }
}
