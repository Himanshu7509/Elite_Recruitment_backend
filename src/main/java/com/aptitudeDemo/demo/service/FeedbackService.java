package com.aptitudeDemo.demo.service;

import java.util.List;

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

    public Feedback saveFeedback(FeedbackReq feedbackReq) {
        log.info("Saving form feedback for: {}", feedbackReq.getRating());
        
        
        Feedback feedback = new Feedback();
        feedback.setName(feedbackReq.getName());

        feedback.setProblem1(feedbackReq.getProblem1());
        feedback.setProblem2(feedbackReq.getProblem2());
        feedback.setProblem3(feedbackReq.getProblem3());
        feedback.setProblem4(feedbackReq.getProblem4());
        feedback.setProblem5(feedbackReq.getProblem5());
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Successfully saved student form with ID: {}", feedback.getId());
        
        return savedFeedback;
    }
    
    public List<Feedback> getAllFeedbacks() {
        log.info("Fetching all student forms");
        return feedbackRepository.findAll();
    }

    public int findRatingByName(String fullName) {

    return feedbackRepository
            .findByName(fullName)
            .map(Feedback::getRating)
            .orElse(0);   // return 0 if feedback not found
}


}
