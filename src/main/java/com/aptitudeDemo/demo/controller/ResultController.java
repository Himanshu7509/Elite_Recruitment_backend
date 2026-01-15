package com.aptitudeDemo.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.ResultReq;
import com.aptitudeDemo.demo.model.student.Result;
import com.aptitudeDemo.demo.service.ResultService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/result")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/all")
    public ResponseEntity<List<Result>> getAllResults() {
        log.info("Received request to fetch all results");
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getResultByStudentFormId(
            @PathVariable String studentFormId
    ) {
        Result result = resultService.getByStudentFormId(studentFormId);

        if (result == null) {
            return ResponseEntity
                    .status(404)
                    .body("Result not found for studentFormId: " + studentFormId);
        }

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/{studentFormId}")
    public ResponseEntity<Result> saveResult(
            @PathVariable String studentFormId,
            @RequestBody ResultReq req) {

        return ResponseEntity.ok(
                resultService.saveResult(studentFormId, req)
        );
    }
    /*
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
    */

}
