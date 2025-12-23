package com.aptitudeDemo.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.student.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {

}
