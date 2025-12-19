package com.aptitudeDemo.demo.model.OpenAI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class DifficultyDistribution {
    private int easy = 33;
    private int medium= 34;
    private int hard = 33;
}
