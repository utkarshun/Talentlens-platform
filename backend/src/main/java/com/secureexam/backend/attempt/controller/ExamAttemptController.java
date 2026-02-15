package com.secureexam.backend.attempt.controller;

import com.secureexam.backend.attempt.dto.AnswerRequest;
import com.secureexam.backend.attempt.service.ExamSubmissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempt")
public class ExamAttemptController {
    private final ExamSubmissionService service;

    public ExamAttemptController(ExamSubmissionService service) {
        this.service = service;
    }

    @PostMapping("/{examId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public com.secureexam.backend.attempt.dto.SubmissionResponse submitExam(
            @PathVariable Long examId,
            @PathVariable Long userId,
            @RequestBody List<AnswerRequest> answers) {
        return service.submitExam(examId, userId, answers);
    }

    @PostMapping("/start/{examId}/{userId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public com.secureexam.backend.attempt.dto.ExamStartResponse startAttempt(
            @PathVariable Long examId,
            @PathVariable Long userId) {
        return service.startAttempt(examId, userId);
    }

    public com.secureexam.backend.attempt.dto.AttemptReviewDTO getAttemptReview(@PathVariable Long attemptId) {
        return service.getAttemptReview(attemptId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public java.util.List<com.secureexam.backend.attempt.dto.AttemptSummaryDTO> getAllAttempts() {
        return service.getAllAttempts();
    }
}
