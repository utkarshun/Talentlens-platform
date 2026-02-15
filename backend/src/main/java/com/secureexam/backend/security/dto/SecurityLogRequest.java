package com.secureexam.backend.security.dto;

import com.secureexam.backend.security.enums.SecurityActivityType;

public record SecurityLogRequest(
        Long attemptId,
        SecurityActivityType activityType,
        String details) {
}
