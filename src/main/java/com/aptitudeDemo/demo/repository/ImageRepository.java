package com.aptitudeDemo.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aptitudeDemo.demo.model.student.Image;

public interface ImageRepository extends MongoRepository<Image, String> {
    Optional<Image> findByStudentFormId(String studentFormId);
    Optional<Image> findByImageUrl(String imageUrl);
}