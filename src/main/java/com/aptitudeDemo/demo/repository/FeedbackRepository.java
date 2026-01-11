package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.student.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    Optional<Feedback> findByName(String name);

    Optional<Feedback> findByStudentFormId(String studentFormId);

}