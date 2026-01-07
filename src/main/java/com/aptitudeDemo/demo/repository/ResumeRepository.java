package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.Resume;


@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {

    Optional<Resume> findByStudentId(String studentId);
}
