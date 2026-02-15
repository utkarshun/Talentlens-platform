package com.secureexam.backend.attempt.dto;

public record AnswerRequest (
    Long questionId,
    String answer
){}
