package com.secureexam.backend.attempt.dto;

public record SubmissionResponse(
        int score,
        int suspiciousScore,
        boolean flagged) {
}
