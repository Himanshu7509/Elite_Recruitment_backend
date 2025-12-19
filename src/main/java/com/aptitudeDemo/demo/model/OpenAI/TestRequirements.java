package com.aptitudeDemo.demo.model.OpenAI;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TestRequirements {
    private int totalQuestions;
    private DifficultyDistribution difficultyDistribution;
    private String questionTypes;
}
