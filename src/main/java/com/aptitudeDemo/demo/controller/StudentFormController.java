package com.aptitudeDemo.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.DashboardDTO;
import com.aptitudeDemo.demo.dto.student.StudentFormRequest;
import com.aptitudeDemo.demo.model.OpenAI.DifficultyDistribution;
import com.aptitudeDemo.demo.model.OpenAI.TestRequest;
import com.aptitudeDemo.demo.model.OpenAI.TestRequirements;
import com.aptitudeDemo.demo.model.student.CandidateProfile;
import com.aptitudeDemo.demo.model.student.StudentForm;
import com.aptitudeDemo.demo.service.FeedbackService;
import com.aptitudeDemo.demo.service.OpenAiService;
import com.aptitudeDemo.demo.service.ResultService;
import com.aptitudeDemo.demo.service.StudentFormService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth/student")
public class StudentFormController {

    

    @Autowired
    private OpenAiService openAiService;


    @Autowired
    private StudentFormService studentFormService;

    @Autowired
    private ResultService resultService;

    @Autowired FeedbackService feedbackService;

    

    @GetMapping("/all")
public ResponseEntity<List<DashboardDTO>> getAllStudents() {

    log.info("Received request to fetch all students");

    // 1. Fetch all student forms
    List<StudentForm> students = studentFormService.getAllStudents();

    // 2. Convert to DashboardDTO
    List<DashboardDTO> dashboardList = students.stream().map(student -> {

        DashboardDTO dto = new DashboardDTO();

        // -------- STUDENT FORM DATA --------
        dto.setFullName(student.getFullName());
        dto.setFatherName(student.getFatherName());
        dto.setPostAppliedFor(student.getPostAppliedFor());
        dto.setReferenceName(student.getReferenceName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAge(student.getAge());
        dto.setMaritalStatus(student.getMaritalStatus());
        dto.setSex(student.getSex());
        dto.setLinkedInProfile(student.getLinkedInProfile());
        dto.setLanguage(student.getLanguage());

        dto.setPermanentAddressLine(student.getPermanentAddressLine());
        dto.setPermanentPin(student.getPermanentPin());
        dto.setPermanentPhone(student.getPermanentPhone());
        dto.setPermanentEmail(student.getPermanentEmail());

        dto.setReference1Name(student.getReference1Name());
        dto.setReference1Mobile(student.getReference1Mobile());
        dto.setReference2Name(student.getReference2Name());
        dto.setReference2Mobile(student.getReference2Mobile());

        dto.setAcademicRecords(student.getAcademicRecords());
        dto.setWorkExperiences(student.getWorkExperiences());

        // -------- FEEDBACK DATA --------
        int rating = feedbackService.findRatingByName(student.getFullName());
        dto.setRating(rating);

        // -------- RESULT DATA --------
        int correctAnswer = resultService.correctAnswerByName(student.getFullName());
        dto.setCorrectAnswer(correctAnswer);

        return dto;
    }).toList();

    return ResponseEntity.ok(dashboardList);
}
    
    @GetMapping("/name/{fullName}")
    public ResponseEntity<?> getStudentByName(@PathVariable String fullName) {
        log.info("Received request to fetch student by exact name: {}", fullName);
        Optional<StudentForm> student = studentFormService.getStudentByName(fullName);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search/{partialName}")
    public ResponseEntity<List<StudentForm>> getStudentsByNameContaining(@PathVariable String partialName) {
        log.info("Received request to search students by partial name: {}", partialName);
        List<StudentForm> students = studentFormService.getStudentsByNameContaining(partialName);
        return ResponseEntity.ok(students);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<?> submitStudentForm(@RequestBody StudentFormRequest studentFormRequest) {
        log.info("Received student form submission for: {}", studentFormRequest.getFullName());

        try {
            StudentForm savedForm = studentFormService.saveStudentForm(studentFormRequest);
            log.info("Successfully saved student form with ID: {}", savedForm.getId());
            
           
            return ResponseEntity.ok(
                openAiService.generateTest(new TestRequest(
                    new CandidateProfile(
                        studentFormRequest.getPostAppliedFor(),
                        studentFormRequest.getExperienceLevel(),
                        studentFormRequest.getYearsOfExperience(),
                        studentFormRequest.getPrimarySkills(),
                        studentFormRequest.getSecondarySkills()
                    ),
                    new TestRequirements(
                        15
                        ,
                        new DifficultyDistribution(
                            33,
                            34,
                            33
                        ),
                        "MCQ"
    
                    ),
                    "JSON"
    
                ))
            );
        } catch (Exception e) {
            log.error("Error saving student form: ", e);
            return ResponseEntity.status(500).body("Error saving student form: " + e.getMessage());
        }
    }
}