package com.aptitudeDemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aptitudeDemo.demo.service.StudentCleanUpService;

@RestController
@RequestMapping("/student")
public class StudentCleanUpController {

    @Autowired
    private StudentCleanUpService cleanupService;

    @DeleteMapping("/{studentFormId}")
    public ResponseEntity<String> deleteStudentCompletely(
            @PathVariable String studentFormId
    ) {
        cleanupService.deleteAllByStudentFormId(studentFormId);
        return ResponseEntity.ok("Student data deleted successfully");
    }
}
