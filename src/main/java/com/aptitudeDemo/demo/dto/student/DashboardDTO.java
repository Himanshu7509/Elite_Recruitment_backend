package com.aptitudeDemo.demo.dto.student;

import java.util.List;

import com.aptitudeDemo.demo.model.student.EducationRecord;
import com.aptitudeDemo.demo.model.student.WorkExperience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    // -------- PERSONAL DATA --------
    private String fullName;
    private String fatherName;
    private String postAppliedFor;
    private String referenceName;
    private String dateOfBirth;
    private int age;
    private String maritalStatus;
    private String sex;
    private String linkedInProfile; // Added LinkedIn profile field

    // -------- LANGUAGE --------
    private String language;

    // -------- PERMANENT ADDRESS -------- (Removed communication address)
    private String permanentAddressLine;
    private String permanentPin;
    private String permanentPhone;
    private String permanentEmail; // Moved email to permanent address section

    // -------- REFERENCES --------
    private String reference1Name;
    private String reference1Mobile;
    private String reference2Name;
    private String reference2Mobile;

    // -------- ACADEMIC RECORD --------
    private List<EducationRecord> academicRecords;

    // -------- WORK EXPERIENCE --------
    private List<WorkExperience> workExperiences;  


    private int rating;
    private int correctAnswer;

}
