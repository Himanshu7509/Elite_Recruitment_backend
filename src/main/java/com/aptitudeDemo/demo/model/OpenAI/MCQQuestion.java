package com.aptitudeDemo.demo.model.OpenAI;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MCQQuestion {
    private String type;
    private String difficulty;
    private String question;
    private List<String> options;
    private int correctAnswer;
}
