package com.aptitudeDemo.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.aptitudeDemo.demo.repository.ResumeRepository;
import com.aptitudeDemo.demo.service.OpenAiService;
import com.aptitudeDemo.demo.service.QuestionsService;
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
    private ResumeRepository resumeRepository;
    
    @Autowired
    private QuestionsService questionsService;
    
    @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getStudentById(
            @PathVariable String studentFormId
    ) {
        return ResponseEntity.ok(
            studentFormService.getByStudentFormId(studentFormId)
             //   studentFormService.getStudentWithResume(studentFormId)
        );
    }
    

    @GetMapping("/all")
    public ResponseEntity<List<StudentForm>> getAllStudents() {
        log.info("Received request to fetch all students");
        List<StudentForm> students = studentFormService.getAllStudents();
        return ResponseEntity.ok(students);
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
            
           
            Object aiGeneratedTest = openAiService.generateTest(new TestRequest(
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
    
            ));
            
            // Save generated questions to database
            try {
                // Parse the generated test and save questions
                if (aiGeneratedTest instanceof List) {
                    List<?> questionList = (List<?>) aiGeneratedTest;
                    // Convert to Question objects and save
                    // This is a simplified version - you may need to adjust based on actual structure
                    log.info("Saving {} generated questions for student {}", 
                        questionList.size(), savedForm.getId());
                    
                    // Create Questions entity and save
                    com.aptitudeDemo.demo.dto.student.QuestionsRequest questionsRequest = 
                        new com.aptitudeDemo.demo.dto.student.QuestionsRequest(
                            studentFormRequest.getPermanentEmail(),
                            studentFormRequest.getFullName(),
                            new java.util.ArrayList<>() // Empty list for now, will be populated by students
                        );
                    
                    questionsService.create(savedForm.getId(), questionsRequest);
                }
            } catch (Exception e) {
                log.warn("Could not save generated questions: {}", e.getMessage());
            }
            
            // Return both the AI-generated test and the saved form ID
            Map<String, Object> response = Map.of(
                "testData", aiGeneratedTest,
                "studentFormId", savedForm.getId(),
                "message", "Student form submitted successfully and test generated"
            );
            
            return ResponseEntity.ok(response);

            } catch (IllegalStateException e) {
        // ✅ Duplicate email case
        log.warn("Duplicate email submission blocked: {}", studentFormRequest.getPermanentEmail());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error saving student form: ", e);
            return ResponseEntity.status(500).body("Error saving student form: " + e.getMessage());
        }
    }
}