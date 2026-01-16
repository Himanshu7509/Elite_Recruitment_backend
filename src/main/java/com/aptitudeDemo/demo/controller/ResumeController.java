package com.aptitudeDemo.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptitudeDemo.demo.model.student.Resume;
import com.aptitudeDemo.demo.service.ResumeS3Service;


@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeS3Service resumeS3Service;
    /*
    @PostMapping("/{studentFormId}")
    public ResponseEntity<?> uploadResume(
            @PathVariable String studentFormId,
            @RequestParam ("file") MultipartFile file
    ) throws IOException {

        resumeS3Service.uploadAndSaveResume(file, studentFormId);
        return ResponseEntity.ok("Resume uploaded successfully");
    }
*/
     @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getResumeByStudentFormId(
            @PathVariable String studentFormId
    ) {
        Resume resume = resumeS3Service.getResumeByStudentFormId(studentFormId);

        if (resume == null) {
            return ResponseEntity
                    .status(404)
                    .body("Resume not found for studentFormId: " + studentFormId);
        }

        return ResponseEntity.ok(resume);
    }

    
    @PostMapping("/upload/{email}")
    public ResponseEntity<?> uploadResume(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        resumeS3Service.uploadAndSaveResume(file, email);

        return ResponseEntity.ok("Resume uploaded successfully");
    }

}


