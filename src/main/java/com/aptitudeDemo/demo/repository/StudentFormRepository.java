package com.aptitudeDemo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.StudentForm;

@Repository
public interface StudentFormRepository extends MongoRepository<StudentForm, String> {
    
    /**
     * Find a student by their full name
     * @param fullName the full name of the student
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<StudentForm> findByFullName(String fullName);
    
    /**
     * Find students whose full name contains the given string (case-insensitive)
     * @param fullName the string to search for in student names
     * @return List of students whose names contain the given string
     */
    @Query("{ 'fullName': { $regex: ?0, $options: 'i' } }")
    List<StudentForm> findByFullNameContainingIgnoreCase(String fullName);
    
    Optional<StudentForm> findByPermanentPhone(String permanentPhone);
    Optional<StudentForm> findByPermanentEmail(String permanentEmail);
}