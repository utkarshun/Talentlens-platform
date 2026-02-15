package com.secureexam.backend.attempt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttemptReviewDTO {
    private Long attemptId;
    private String studentName;
    private String examTitle;
    private int score;
    private int totalQuestions;
    private List<QuestionReviewDTO> questions;
    private List<String> suspiciousLogs;
}
