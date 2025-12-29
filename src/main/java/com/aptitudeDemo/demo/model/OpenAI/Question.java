package com.aptitudeDemo.demo.model.OpenAI;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Question {

    private String aiQuestion;
    private List<String> Options;
    private String aiAnswer;
    private String userAnswer;
    
}
