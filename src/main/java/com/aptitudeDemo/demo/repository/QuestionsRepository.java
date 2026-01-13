package com.aptitudeDemo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.OpenAI.Questions;

@Repository
public interface QuestionsRepository extends MongoRepository<Questions, String> {

    List<Questions> findByEmail(String email);

    Optional<Questions> findByStudentFormId(String studentFormId);
    
    void deleteByStudentFormId(String studentFormId);

}
