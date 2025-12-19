package com.aptitudeDemo.demo.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.aptitudeDemo.demo.model.OpenAI.TestRequest;

import tools.jackson.databind.ObjectMapper;

@Service
public class OpenAiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SYSTEM_PROMPT = """
        You are a senior technical interviewer and assessment designer.
        Create job-specific aptitude and technical tests.
        Questions must be realistic, industry-relevant, and evaluative.
        Avoid generic questions.
        """;

    public OpenAiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateTest(TestRequest request) throws Exception {

        String userPrompt = buildUserPrompt(request);

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-nano",
                "messages", new Object[]{
                        Map.of("role", "system", "content", SYSTEM_PROMPT),
                        Map.of("role", "user", "content", userPrompt)
                },
                "temperature", 0.4
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response ->
                        ((Map)((Map)((java.util.List) response.get("choices")).get(0))
                                .get("message")).get("content").toString()
                )
                .block();
    }

    private String buildUserPrompt(TestRequest request) throws Exception {
        return """
        Generate exactly 30 MCQ questions with the following rules:

        - 10 Easy, 10 Medium, 10 Hard
        - Each question must follow this JSON format:
        [
          {
            "type": "MCQ",
            "difficulty": "Easy|Medium|Hard",
            "question": "Q[1-31]. ...",
            "options": ["A","B","C","D"],
            "correctAnswer": A-E
          }
        ]

        Candidate Profile:
        %s

        Test Requirements:
        %s

        Output ONLY valid JSON array. No explanations. No markdown.
        """
        .formatted(
            objectMapper.writeValueAsString(request.getCandidateProfile()),
            objectMapper.writeValueAsString(request.getTestRequirements())
        );
    }
}
