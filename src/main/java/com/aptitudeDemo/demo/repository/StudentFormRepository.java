package com.aptitudeDemo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.aptitudeDemo.demo.model.student.StudentForm;

@Repository
public interface StudentFormRepository extends MongoRepository<StudentForm, String> {
    
    Optional<StudentForm> findByFullName(String fullName);
    
    @Query("{ 'fullName': { $regex: ?0, $options: 'i' } }")
    List<StudentForm> findByFullNameContainingIgnoreCase(String fullName);
    
    Optional<StudentForm> findByPermanentPhone(String permanentPhone);
    Optional<StudentForm> findByPermanentEmail(String permanentEmail);

    boolean existsByPermanentEmail(String permanentEmail);
}