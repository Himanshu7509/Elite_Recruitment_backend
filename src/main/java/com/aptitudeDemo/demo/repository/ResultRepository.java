package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.student.Result;

public interface ResultRepository extends MongoRepository<Result,String> {

    Optional<Result> findByFullName(String fullName);
    Optional<Result> findByStudentFormId(String studentFormId);

}
