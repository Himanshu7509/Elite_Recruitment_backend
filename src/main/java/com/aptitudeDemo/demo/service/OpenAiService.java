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
       You are required to generate a technical aptitude test.

CRITICAL INSTRUCTION (DO NOT IGNORE):
- The total number of questions is defined by TestRequirements.totalQuestions.
- You MUST generate EXACTLY that many MCQ questions.
- Generating fewer or more questions makes the response INVALID.

PROCESS (MANDATORY):
1. Read Test Requirements.
2. Extract the value of TestRequirements.totalQuestions.
3. Let this value be N.
4. Generate EXACTLY N questions — no more, no less.

Each question must follow this JSON format:
[
  {
    "type": "MCQ",
    "difficulty": "Easy|Medium|Hard",
    "question": "Q[1-N]. ...",
    "options": ["A","B","C","D"],
    "correctAnswer": "A|B|C|D"
  }
]

STRICT RULES:
- Question numbering MUST start at Q1 and end at QN
- No missing or extra question numbers are allowed
- Do NOT shuffle questions
- Order MUST be:
  - All Easy questions first
  - Then all Medium questions
  - Then all Hard questions

Candidate Profile:
%s

Test Requirements:
%s

CONTENT RULES:
- Use TestRequirements.totalQuestions as the ONLY source of total count
- Generate 70%% of N questions based on candidate skills
- Generate 30%% of N questions based on the applied position
- Questions must match the candidate’s experience level
- Avoid generic or textbook-style questions
- Use real-world, industry-relevant scenarios

UNIQUENESS RULES:
- Ensure global uniqueness across all candidates
- Do NOT reuse, paraphrase, or structurally imitate any previous question
- Use entirely different scenarios, datasets, constraints, and contexts
- Treat the question pool as cumulative and permanent
- If any similarity risk exists, discard and generate a new question

FAILURE CONDITIONS (ANY = INVALID RESPONSE):
- Total questions ≠ TestRequirements.totalQuestions
- Question numbering does not end at QN
- Any question is missing required fields
- Output is not a valid JSON array

FINAL CHECK (MANDATORY):
- Before responding, verify that the JSON array length equals N
- Verify the last question is labeled QN

OUTPUT RULES:
- Output ONLY a valid JSON array
- Do not include explanations, comments, or metadata
- No markdown

        """.formatted(
                objectMapper.writeValueAsString(request.getCandidateProfile()),
                objectMapper.writeValueAsString(request.getTestRequirements())
        );
    }
}
