package com.aptitudeDemo.demo.model.student;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CandidateProfile {


    private String postAppliedFor;
    private String experienceLevel;
    private int yearsOfExperience;
    private List<String> primarySkills;
    private List<String> secondarySkills;
}
