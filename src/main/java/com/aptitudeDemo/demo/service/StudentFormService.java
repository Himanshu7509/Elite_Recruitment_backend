package com.aptitudeDemo.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.EducationRecordDto;
import com.aptitudeDemo.demo.dto.student.StudentFormRequest;
import com.aptitudeDemo.demo.dto.student.StudentProfileResponse;
import com.aptitudeDemo.demo.dto.student.WorkExperienceDto;
import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.model.student.EducationRecord;
import com.aptitudeDemo.demo.model.student.Feedback;
import com.aptitudeDemo.demo.model.student.Result;
import com.aptitudeDemo.demo.model.student.Resume;
import com.aptitudeDemo.demo.model.student.StudentForm;
import com.aptitudeDemo.demo.model.student.WorkExperience;
import com.aptitudeDemo.demo.repository.StudentFormRepository;
import com.aptitudeDemo.demo.repository.FeedbackRepository;
import com.aptitudeDemo.demo.repository.ResultRepository;
import com.aptitudeDemo.demo.repository.ResumeRepository;
import com.aptitudeDemo.demo.repository.QuestionsRepository;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentFormService {

    @Autowired
    private StudentFormRepository studentFormRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private QuestionsRepository questionsRepository;


    public StudentForm saveStudentForm(StudentFormRequest studentFormRequest) {
        log.info("Saving student form for: {}", studentFormRequest.getFullName());

        String email = studentFormRequest.getPermanentEmail()
                .trim()
                .toLowerCase();

        if (studentFormRepository.existsByPermanentEmail(email)) {
            log.warn("Duplicate email detected: {}", email);
            throw new IllegalStateException(
                "This email is already registered. You cannot submit the form again."
            );
        }

        StudentForm studentForm = new StudentForm();

        studentForm.setFullName(studentFormRequest.getFullName());
        studentForm.setFatherName(studentFormRequest.getFatherName());
        studentForm.setPostAppliedFor(studentFormRequest.getPostAppliedFor());
        studentForm.setReferenceName(studentFormRequest.getReferenceName());
        studentForm.setDateOfBirth(studentFormRequest.getDateOfBirth());
        studentForm.setAge(studentFormRequest.getAge());
        studentForm.setMaritalStatus(studentFormRequest.getMaritalStatus());
        studentForm.setSex(studentFormRequest.getSex());
        studentForm.setLinkedInProfile(studentFormRequest.getLinkedInProfile());
        
        studentForm.setPermanentAddressLine(studentFormRequest.getPermanentAddressLine());
        studentForm.setPermanentPin(studentFormRequest.getPermanentPin());
        studentForm.setPermanentPhone(studentFormRequest.getPermanentPhone());
        studentForm.setPermanentEmail(studentFormRequest.getPermanentEmail());
        
        studentForm.setReference1Name(studentFormRequest.getReference1Name());
        studentForm.setReference1Mobile(studentFormRequest.getReference1Mobile());
        studentForm.setReference2Name(studentFormRequest.getReference2Name());
        studentForm.setReference2Mobile(studentFormRequest.getReference2Mobile());
        
        studentForm.setLanguage(studentFormRequest.getLanguage());
        studentForm.setExperienceLevel(studentFormRequest.getExperienceLevel());
        studentForm.setYearsOfExperience(studentFormRequest.getYearsOfExperience());
        studentForm.setPrimarySkills(studentFormRequest.getPrimarySkills());
        studentForm.setSecondarySkills(studentFormRequest.getSecondarySkills());
        
        studentForm.setAcademicRecords(convertEducationRecords(studentFormRequest.getAcademicRecords()));
        studentForm.setWorkExperiences(convertWorkExperiences(studentFormRequest.getWorkExperiences()));
        
        StudentForm savedForm = studentFormRepository.save(studentForm);
        log.info("Successfully saved student form with ID: {}", savedForm.getId());
        
        return savedForm;
    }
    
    public List<StudentForm> getAllStudents() {
        log.info("Fetching all student forms");
        return studentFormRepository.findAll();
    }
    
    public Optional<StudentForm> getStudentByName(String fullName) {
        log.info("Fetching student form by name: {}", fullName);
        return studentFormRepository.findByFullName(fullName);
    }
    
    public List<StudentForm> getStudentsByNameContaining(String partialName) {
        log.info("Fetching student forms containing name: {}", partialName);
        return studentFormRepository.findByFullNameContainingIgnoreCase(partialName);
    }
    
    public boolean verifyStudentMobileNumber(String studentId, String mobileNumber) {
        log.info("Verifying mobile number for student ID: {}", studentId);
        Optional<StudentForm> studentForm = studentFormRepository.findById(studentId);
        if (studentForm.isPresent()) {
            StudentForm student = studentForm.get();
            if (mobileNumber.equals(student.getPermanentPhone())) {
                return true;
            }
        }
        return false;
    }
    
    public StudentProfileResponse getByStudentFormId(String studentFormId) {

        log.info("Fetching complete profile for studentFormId: {}", studentFormId);

        StudentForm studentForm = studentFormRepository
                .findById(studentFormId)
                .orElseThrow(() -> new RuntimeException("StudentForm not found"));

    return new StudentProfileResponse(
            studentForm,
            feedbackRepository.findByStudentFormId(studentFormId).orElse(null),
            resultRepository.findByStudentFormId(studentFormId).orElse(null),
            resumeRepository.findByStudentFormId(studentFormId).orElse(null),
            questionsRepository.findByStudentFormId(studentFormId).orElse(null)
    );
    
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