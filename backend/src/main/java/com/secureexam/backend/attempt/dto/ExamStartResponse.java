package com.secureexam.backend.attempt.dto;

import com.secureexam.backend.question.dto.QuestionResponse;
import java.util.List;

public record ExamStartResponse(
        Long attemptId,
        Long examId,
        String examTitle,
        int durationMinutes,
        List<QuestionResponse> questions) {
}
