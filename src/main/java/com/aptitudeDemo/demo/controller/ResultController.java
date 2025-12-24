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

import com.aptitudeDemo.demo.dto.student.ResultReq;
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

    @GetMapping("/all")
    public ResponseEntity<List<Result>> getAllResults() {
        log.info("Received request to fetch all results");
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getResultById(@PathVariable String id) {
        log.info("Received request to fetch result by ID: {}", id);
        Optional<Result> result = resultService.getResultById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
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
