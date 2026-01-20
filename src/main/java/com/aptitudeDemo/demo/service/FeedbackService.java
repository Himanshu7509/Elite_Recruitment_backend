package com.aptitudeDemo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.FeedbackReq;
import com.aptitudeDemo.demo.model.student.Feedback;
import com.aptitudeDemo.demo.repository.FeedbackRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(String studentFormId, FeedbackReq feedbackReq) {
    	
    	if (studentFormId == null || studentFormId.isBlank()) {
            throw new IllegalArgumentException("studentFormId is mandatory");
        }
        
        // Validate that studentFormId is a proper MongoDB ObjectId format
        if (!isValidMongoId(studentFormId)) {
            log.warn("Invalid studentFormId format detected: {}. Expected MongoDB ObjectId format.", studentFormId);
            throw new IllegalArgumentException("Invalid studentFormId format: " + studentFormId + ". Expected MongoDB ObjectId format.");
        }
        
        // Check if feedback already exists for this studentFormId
        Optional<Feedback> existingFeedback = feedbackRepository.findByStudentFormId(studentFormId);
        if (existingFeedback.isPresent()) {
            log.info("Updating existing feedback for studentFormId: {}", studentFormId);
            Feedback feedback = existingFeedback.get();
            updateFeedbackFields(feedback, feedbackReq);
            Feedback updatedFeedback = feedbackRepository.save(feedback);
            log.info("Successfully updated feedback with ID: {}", updatedFeedback.getId());
            return updatedFeedback;
        }
        
        log.info("Saving new feedback for studentFormId: {} with rating: {}", studentFormId, feedbackReq.getRating());
        
        Feedback feedback = new Feedback();
        feedback.setStudentFormId(studentFormId);
        updateFeedbackFields(feedback, feedbackReq);
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Successfully saved new feedback with ID: {} for studentFormId: {}", savedFeedback.getId(), studentFormId);
        
        return savedFeedback;
    }

    public Feedback getFeedbackByStudentById(String studentFormId){
        return feedbackRepository.findByStudentFormId(studentFormId).orElse(null);
    }
    
    
    public List<Feedback> getAllFeedbacks() {
        log.info("Fetching all student forms");
        return feedbackRepository.findAll();
    }

    public int findRatingByName(String fullName) {

    return feedbackRepository
            .findByName(fullName)
            .map(Feedback::getRating)
            .orElse(0);
}

    private boolean isValidMongoId(String id) {
        // MongoDB ObjectId is 24 hexadecimal characters
        return id != null && id.matches("^[0-9a-fA-F]{24}$");
    }
    
    private void updateFeedbackFields(Feedback feedback, FeedbackReq feedbackReq) {
        feedback.setName(feedbackReq.getName());
        feedback.setRating(feedbackReq.getRating());
        feedback.setProblem1(feedbackReq.getProblem1());
        feedback.setProblem2(feedbackReq.getProblem2());
        feedback.setProblem3(feedbackReq.getProblem3());
        feedback.setProblem4(feedbackReq.getProblem4());
        feedback.setProblem5(feedbackReq.getProblem5());
    }
    
    public void deleteFeedbackByStudentFormId(String studentFormId) {
        if (studentFormId == null || studentFormId.isBlank()) {
            throw new IllegalArgumentException("studentFormId is mandatory for deletion");
        }
        
        feedbackRepository.deleteByStudentFormId(studentFormId);
        log.info("Deleted feedback for studentFormId: {}", studentFormId);
    }


}
