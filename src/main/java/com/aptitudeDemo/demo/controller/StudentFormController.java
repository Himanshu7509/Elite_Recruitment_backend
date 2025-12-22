package com.aptitudeDemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.StudentFormRequest;
import com.aptitudeDemo.demo.model.OpenAI.DifficultyDistribution;
import com.aptitudeDemo.demo.model.OpenAI.TestRequest;
import com.aptitudeDemo.demo.model.OpenAI.TestRequirements;
import com.aptitudeDemo.demo.model.student.CandidateProfile;
import com.aptitudeDemo.demo.model.student.StudentForm;
import com.aptitudeDemo.demo.service.OpenAiService;
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
                        30,
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