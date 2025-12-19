package com.aptitudeDemo.demo.model.OpenAI;

import com.aptitudeDemo.demo.model.student.CandidateProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TestRequest {
    private CandidateProfile candidateProfile;
    private TestRequirements testRequirements;
    private String outputFormat;
}