package com.secureexam.backend.attempt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReviewDTO {
    private String questionText;
    private String studentAnswer;
    private String correctAnswer;
    private boolean isCorrect;
    private List<String> options; // [A, B, C, D]
}
