package com.secureexam.backend.question.controller;

import com.secureexam.backend.exam.entity.Question;
import com.secureexam.backend.question.dto.QuestionResponse;
import com.secureexam.backend.question.service.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/{examId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Question addQuestion(
            @PathVariable Long examId,
            @RequestBody Question question) {
        return questionService.addQuestion(examId, question);
    }
    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public List<QuestionResponse> getExamQuestions(
            @PathVariable Long examId
    ){
        return questionService.getShuffledQuestions(examId);
    }
}
