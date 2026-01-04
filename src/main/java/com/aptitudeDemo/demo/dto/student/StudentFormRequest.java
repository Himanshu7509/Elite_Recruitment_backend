package com.aptitudeDemo.demo.dto.student;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class StudentFormRequest {
    
    private String fullName;
    private String fatherName;
    private String postAppliedFor;
    private String referenceName;
    private String dateOfBirth;
    private int age;
    private String maritalStatus;
    private String sex;
    private String linkedInProfile;

    private String language;

    private String permanentAddressLine;
    private String permanentPin;
    private String permanentPhone;
    private String permanentEmail;

    private String reference1Name;
    private String reference1Mobile;
    private String reference2Name;
    private String reference2Mobile;

    private List<EducationRecordDto> academicRecords;

    private List<WorkExperienceDto> workExperiences;

    private String experienceLevel;
    private int yearsOfExperience;
    private List<String> primarySkills;
    private List<String> secondarySkills;
}