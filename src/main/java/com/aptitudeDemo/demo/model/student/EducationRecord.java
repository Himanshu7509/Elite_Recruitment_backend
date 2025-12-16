package com.aptitudeDemo.demo.model.student;

import lombok.Data;

@Data
public class EducationRecord {
    private String schoolOrCollege;
    private String boardOrUniversity;
    private String examinationPassed;
    private int yearOfPassing;
    private String mainSubjects;
    private String percentage;
}