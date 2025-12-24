package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.student.Result;

public interface ResultRepository extends MongoRepository<Result,String> {

    Optional<Result> findByFullName(String fullName);
    
    // The findById method is inherited from MongoRepository
    // Optional<Result> findById(String id);
    
    // getAll is also available through findAll() method inherited from MongoRepository
}
