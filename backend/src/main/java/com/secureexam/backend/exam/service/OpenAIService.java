package com.secureexam.backend.exam.service;

import com.secureexam.backend.exam.dto.openai.ChatCompletionRequest;
import com.secureexam.backend.exam.dto.openai.ChatCompletionResponse;
import com.secureexam.backend.exam.dto.openai.Message;
import com.secureexam.backend.exam.entity.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Question> generateQuestions(String topic, int count) {
        String prompt = createPrompt(topic, count);
        ChatCompletionRequest request = new ChatCompletionRequest("gpt-3.5-turbo", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ChatCompletionResponse> response = restTemplate.postForEntity(apiUrl, entity,
                    ChatCompletionResponse.class);
            if (response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                String content = response.getBody().getChoices().get(0).getMessage().getContent();
                return parseQuestions(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // In a real app, handle exceptions better (custom exception)
        }
        return new ArrayList<>();
    }

    private String createPrompt(String topic, int count) {
        return String.format("Generate %d multiple-choice questions about '%s'. " +
                "Return the response ONLY as a raw JSON array of objects. " +
                "Do not include any markdown formatting like ```json. " +
                "Each object must have these fields: " +
                "questionText, optionA, optionB, optionC, optionD, correctAnswer (which must be one of the option values).",
                count, topic);
    }

    private List<Question> parseQuestions(String jsonContent) {
        try {
            // Clean up if the model adds markdown
            String cleanJson = jsonContent.replace("```json", "").replace("```", "").trim();
            return objectMapper.readValue(cleanJson, new TypeReference<List<Question>>() {
            });
        } catch (Exception e) {
            System.err.println("Failed to parse OpenAI response: " + jsonContent);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
