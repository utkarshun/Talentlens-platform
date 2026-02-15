package com.secureexam.backend.attempt.dto;

public record AttemptSummaryDTO(
        Long id,
        StudentSummary student,
        ExamSummary exam,
        int score,
        int suspiciousScore,
        boolean flagged) {
    public record StudentSummary(String email) {
    }

    public record ExamSummary(String title) {
    }
}
