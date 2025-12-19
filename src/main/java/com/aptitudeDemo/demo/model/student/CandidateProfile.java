package com.aptitudeDemo.demo.model.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CandidateProfile {

    @Autowired
    StudentForm s = new StudentForm();

    private String role = s.getPostAppliedFor();
    private String experienceLevel;
    private int yearsOfExperience;
    private List<String> primarySkills;
    private List<String> secondarySkills;
}
