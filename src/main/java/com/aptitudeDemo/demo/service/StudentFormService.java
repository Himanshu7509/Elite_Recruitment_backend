package com.aptitudeDemo.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.EducationRecordDto;
import com.aptitudeDemo.demo.dto.student.StudentFormRequest;
import com.aptitudeDemo.demo.dto.student.WorkExperienceDto;
import com.aptitudeDemo.demo.model.student.EducationRecord;
import com.aptitudeDemo.demo.model.student.StudentForm;
import com.aptitudeDemo.demo.model.student.WorkExperience;
import com.aptitudeDemo.demo.repository.StudentFormRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentFormService {

    @Autowired
    private StudentFormRepository studentFormRepository;

    public StudentForm saveStudentForm(StudentFormRequest studentFormRequest) {
        log.info("Saving student form for: {}", studentFormRequest.getFullName());
        
        StudentForm studentForm = new StudentForm();
        // Copy all primitive properties
        studentForm.setFullName(studentFormRequest.getFullName());
        studentForm.setFatherName(studentFormRequest.getFatherName());
        studentForm.setPostAppliedFor(studentFormRequest.getPostAppliedFor());
        studentForm.setReferenceName(studentFormRequest.getReferenceName());
        studentForm.setDateOfBirth(studentFormRequest.getDateOfBirth());
        studentForm.setAge(studentFormRequest.getAge());
        studentForm.setMaritalStatus(studentFormRequest.getMaritalStatus());
        studentForm.setSex(studentFormRequest.getSex());
        studentForm.setLinkedInProfile(studentFormRequest.getLinkedInProfile()); // Added LinkedIn profile
        
        // Copy skills and experience information
        studentForm.setPrimarySkills(studentFormRequest.getPrimarySkills());
        studentForm.setSecondarySkills(studentFormRequest.getSecondarySkills());
        studentForm.setYearsOfExperience(studentFormRequest.getYearsOfExperience());
        
        // Copy permanent address information (removed communication address)
        studentForm.setPermanentAddressLine(studentFormRequest.getPermanentAddressLine());
        studentForm.setPermanentPin(studentFormRequest.getPermanentPin());
        studentForm.setPermanentPhone(studentFormRequest.getPermanentPhone());
        studentForm.setPermanentEmail(studentFormRequest.getPermanentEmail()); // Moved email to permanent address
        
        // Copy reference information
        studentForm.setReference1Name(studentFormRequest.getReference1Name());
        studentForm.setReference1Mobile(studentFormRequest.getReference1Mobile());
        studentForm.setReference2Name(studentFormRequest.getReference2Name());
        studentForm.setReference2Mobile(studentFormRequest.getReference2Mobile());
        
        // Copy language
        studentForm.setLanguage(studentFormRequest.getLanguage());
        
        StudentForm savedForm = studentFormRepository.save(studentForm);
        log.info("Successfully saved student form with ID: {}", savedForm.getId());
        
        return savedForm;
    }
    
    private List<EducationRecord> convertEducationRecords(List<EducationRecordDto> educationRecordDtos) {
        if (educationRecordDtos == null) {
            return new ArrayList<>();
        }
        
        List<EducationRecord> educationRecords = new ArrayList<>();
        for (EducationRecordDto dto : educationRecordDtos) {
            EducationRecord educationRecord = new EducationRecord();
            educationRecord.setSchoolOrCollege(dto.getSchoolOrCollege());
            educationRecord.setBoardOrUniversity(dto.getBoardOrUniversity());
            educationRecord.setExaminationPassed(dto.getExaminationPassed());
            educationRecord.setYearOfPassing(dto.getYearOfPassing());
            educationRecord.setMainSubjects(dto.getMainSubjects());
            educationRecord.setPercentage(dto.getPercentage());
            educationRecords.add(educationRecord);
        }
        return educationRecords;
    }
    
    private List<WorkExperience> convertWorkExperiences(List<WorkExperienceDto> workExperienceDtos) {
        if (workExperienceDtos == null) {
            return new ArrayList<>();
        }
        
        List<WorkExperience> workExperiences = new ArrayList<>();
        for (WorkExperienceDto dto : workExperienceDtos) {
            WorkExperience workExperience = new WorkExperience();
            workExperience.setEmployerName(dto.getEmployerName());
            workExperience.setDurationFrom(dto.getDurationFrom());
            workExperience.setDurationTo(dto.getDurationTo());
            workExperience.setDesignation(dto.getDesignation());
            workExperience.setBriefJobProfile(dto.getBriefJobProfile());
            workExperience.setTotalSalary(dto.getTotalSalary());
            workExperience.setJoiningDate(dto.getJoiningDate());
            workExperience.setLastDate(dto.getLastDate());
            workExperiences.add(workExperience);
        }
        return workExperiences;
    }
}