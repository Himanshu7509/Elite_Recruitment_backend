package com.aptitudeDemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.FeedbackReq;
import com.aptitudeDemo.demo.dto.student.ResultReq;
import com.aptitudeDemo.demo.model.student.Feedback;
import com.aptitudeDemo.demo.model.student.Result;
import com.aptitudeDemo.demo.service.ResultService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/result")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitResult(@RequestBody ResultReq resultReq) {
        
        try {
            Result savedResult= resultService.saveResult(resultReq);
            log.info("Successfully saved student form with ID: {}", resultReq.getId());
            
           
            return ResponseEntity.ok(savedResult);
        } catch (Exception e) {
            log.error("Error saving student form: ", e);
            return ResponseEntity.status(500).body("Error saving student form: " + e.getMessage());
        }
    }

}
