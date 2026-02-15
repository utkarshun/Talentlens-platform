package com.secureexam.backend.exam.dto.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    public ChatCompletionRequest(String model, String prompt) {
        this.model = model;
        this.messages = List.of(new Message("user", prompt));
        this.temperature = 0.7;
    }
}
