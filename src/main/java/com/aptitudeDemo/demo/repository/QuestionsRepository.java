package com.aptitudeDemo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.OpenAI.Questions;

public interface QuestionsRepository extends MongoRepository<Questions, String> {

    List<Questions> findByEmail(String email);

    Optional<Questions> findByStudentFormId(String studentFormId);

}
