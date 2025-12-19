package com.aptitudeDemo.demo.model.student;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.aptitudeDemo.demo.dto.student.EducationRecordDto;
import com.aptitudeDemo.demo.dto.student.WorkExperienceDto;

import lombok.Data;

@Data
@Document(collection = "student_forms")
public class StudentForm {

    @Id
    private String id;

    // -------- PERSONAL DATA --------
    private String fullName;
    private String fatherName;
    private String dateOfBirth;
    private int age;
    private String maritalStatus;
    private String sex;
    private String permanentPhone;
    private String permanentEmail;
    private String language;
    private String permanentAddressLine;
    private String permanentPin;
    private String reference1Name;
    private String reference1Mobile;
    private String reference2Name;
    private String reference2Mobile;
    private String referenceName;

    // -------- ACADEMIC RECORD --------
    private List<EducationRecordDto> academicRecords;


    private String postAppliedFor;
    private double yearsOfExperience;
    private String experienceLevel;
    // -------- WORK EXPERIENCE --------
    private List<WorkExperienceDto> workExperiences;
    
    private String linkedInProfile; 
    // -------- LANGUAGE --------
    
    
    // -------- SKILLS AND EXPERIENCE --------
    private List<String> primarySkills;
    private List<String> secondarySkills;
}