package com.aptitudeDemo.demo.model.student;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
@Document(collection = "student_forms")
public class StudentForm {

    @Id
    private String id;

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

   // @Indexed(unique=true)
    private String permanentEmail;

    private String reference1Name;
    private String reference1Mobile;
    private String reference2Name;
    private String reference2Mobile;

    private List<EducationRecord> academicRecords;

    private List<WorkExperience> workExperiences;

    private String experienceLevel;
    private int yearsOfExperience;
    private List<String> primarySkills;
    private List<String> secondarySkills;
    
    private String resumeId;
    private String resumeUrl;

}