package com.aptitudeDemo.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptitudeDemo.demo.service.ResumeS3Service;


@RestController
@RequestMapping("/api/resume")
@CrossOrigin
public class ResumeController {

    @Autowired
    private ResumeS3Service resumeS3Service;

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<?> uploadResume(
            @PathVariable String studentId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        resumeS3Service.uploadAndSaveResume(file, studentId);

        return ResponseEntity.ok("Resume uploaded successfully");
    }
}
