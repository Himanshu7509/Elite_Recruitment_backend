package com.aptitudeDemo.demo.dto.student;

import lombok.Data;

@Data
public class EducationRecordDto {
    private String schoolOrCollege;
    private String boardOrUniversity;
    private String examinationPassed;
    private int yearOfPassing;
    private String mainSubjects;
    private String percentage;
}