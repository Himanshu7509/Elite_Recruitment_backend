package com.aptitudeDemo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.FeedbackReq;
import com.aptitudeDemo.demo.model.student.Feedback;
import com.aptitudeDemo.demo.service.FeedbackService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        log.info("Received request to fetch all students");
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @PostMapping("/{studentFormId}")
    public ResponseEntity<Feedback> saveFeedback(
            @PathVariable String studentFormId,
            @RequestBody FeedbackReq feedbackReq) {

        return ResponseEntity.ok(
                feedbackService.saveFeedback(studentFormId, feedbackReq)
        );
    }

    @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getFeedback(@PathVariable String studentFormId){
        Feedback feedback = feedbackService.getFeedbackByStudentById(studentFormId);

        if (feedback == null) {
            return ResponseEntity
                    .status(404)
                    .body("feedback not found for studentFormId: " + studentFormId);
        }
        return ResponseEntity.ok(feedback);
    }
    
    @DeleteMapping("/{studentFormId}")
    public ResponseEntity<?> deleteFeedback(@PathVariable String studentFormId) {
        try {
            feedbackService.deleteFeedbackByStudentFormId(studentFormId);
            return ResponseEntity.ok("Feedback deleted successfully for studentFormId: " + studentFormId);
        } catch (Exception e) {
            log.error("Error deleting feedback for studentFormId: {}: {}", studentFormId, e.getMessage());
            return ResponseEntity.status(500).body("Error deleting feedback: " + e.getMessage());
        }
    }
    
    /*
    @PostMapping("/submit")
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackReq feedbackReq) {
        
        try {
            Feedback savedFeedback= feedbackService.saveFeedback(feedbackReq);
            log.info("Successfully saved student form with ID: {}", savedFeedback.getId());
            
           
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            log.error("Error saving student form: ", e);
            return ResponseEntity.status(500).body("Error saving student form: " + e.getMessage());
        }
    }
*/
}
