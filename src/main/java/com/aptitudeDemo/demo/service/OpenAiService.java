package com.aptitudeDemo.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.aptitudeDemo.demo.model.OpenAI.TestRequest;

import tools.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.model}")
    private String model;

    private static final String SYSTEM_PROMPT = """
        You are a senior technical interviewer and assessment designer.
        Create job-specific aptitude and technical tests.
        Questions must be realistic, industry-relevant, and evaluative.
        Avoid generic questions.
        """;

    public OpenAiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String generateTest(TestRequest request) throws Exception {

        String userPrompt = buildUserPrompt(request);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.4
        );

        Map response = restClient.post()
                .uri("/chat/completions")
                .body(body)
                .retrieve()
                .body(Map.class);

        return ((Map) ((Map)
                ((List) response.get("choices"))
                        .get(0))
                .get("message"))
                .get("content")
                .toString();
    }

    private String buildUserPrompt(TestRequest request) throws Exception {
        return """
        Generate exactly 30 MCQ questions with the following rules:

        - 10 Easy, 10 Medium, 10 Hard
        - Output STRICT JSON array only

        Candidate Profile:
        %s

        Test Requirements:
        %s
        """.formatted(
                objectMapper.writeValueAsString(request.getCandidateProfile()),
                objectMapper.writeValueAsString(request.getTestRequirements())
        );
    }
}
