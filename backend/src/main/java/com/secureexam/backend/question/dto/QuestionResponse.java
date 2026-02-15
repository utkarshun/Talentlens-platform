package com.secureexam.backend.question.dto;

import java.util.List;

public record QuestionResponse(
        Long id,
        String questionText,

        List<String> options) {
}
