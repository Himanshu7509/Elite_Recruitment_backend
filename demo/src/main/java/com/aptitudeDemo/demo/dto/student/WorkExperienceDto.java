package com.aptitudeDemo.demo.dto.student;

import lombok.Data;

@Data
public class WorkExperienceDto {
    private String employerName;
    private String durationFrom;
    private String durationTo;
    private String designation;
    private String briefJobProfile;
    private String totalSalary;
    private String joiningDate;
    private String lastDate;
}