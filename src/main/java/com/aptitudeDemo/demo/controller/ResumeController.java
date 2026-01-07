package com.aptitudeDemo.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
