package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.Feedback;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    Optional<Feedback> findByName(String name);

    Optional<Feedback> findByStudentFormId(String studentFormId);

}