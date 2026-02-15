package com.secureexam.backend.exam.controller;

import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.exam.service.ExamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @org.springframework.web.bind.annotation.GetMapping
    public java.util.List<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @PostMapping("/{examId}/generate-questions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public java.util.List<com.secureexam.backend.exam.entity.Question> generateQuestions(
            @org.springframework.web.bind.annotation.PathVariable Long examId,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "5") int count) {
        return examService.generateQuestionsForExam(examId, count);
    }
}
