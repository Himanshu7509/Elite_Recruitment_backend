package com.aptitudeDemo.demo.model.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class DifficultyDistribution {
    private int easy;
    private int medium;
    private int hard ;
}
