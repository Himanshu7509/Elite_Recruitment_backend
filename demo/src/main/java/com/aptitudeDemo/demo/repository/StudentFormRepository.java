package com.aptitudeDemo.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.StudentForm;

@Repository
public interface StudentFormRepository extends MongoRepository<StudentForm, String> {
}