package com.secureexam.backend.exam.service;

import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.exam.repository.ExamRepository;
import com.secureexam.backend.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final OpenAIService openAIService;
    private final QuestionRepository questionRepository;

    public ExamService(ExamRepository examRepository, OpenAIService openAIService,
            QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.openAIService = openAIService;
        this.questionRepository = questionRepository;
    }

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public List<Question> generateQuestionsForExam(Long examId, int count) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<Question> questions = openAIService.generateQuestions(exam.getTitle(), count);

        for (Question question : questions) {
            question.setExam(exam);
        }

        return questionRepository.saveAll(questions);
    }
}
