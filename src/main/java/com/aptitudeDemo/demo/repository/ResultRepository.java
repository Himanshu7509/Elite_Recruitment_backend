package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.Result;

@Repository
public interface ResultRepository extends MongoRepository<Result,String> {

    Optional<Result> findByFullName(String fullName);
    Optional<Result> findByStudentFormId(String studentFormId);

}
