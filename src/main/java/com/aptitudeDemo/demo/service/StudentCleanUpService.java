package com.aptitudeDemo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.repository.*;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Service
public class StudentCleanUpService {

    @Autowired private StudentFormRepository studentFormRepository;
    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private ResultRepository resultRepository;
    @Autowired private QuestionsRepository questionsRepository;
    @Autowired private ResumeRepository resumeRepository;
    @Autowired private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public void deleteAllByStudentFormId(String studentFormId) {

        feedbackRepository.deleteByStudentFormId(studentFormId);
        resultRepository.deleteByStudentFormId(studentFormId);
        questionsRepository.deleteByStudentFormId(studentFormId);

        resumeRepository.findByStudentFormId(studentFormId)
                .ifPresent(resume -> {
                    s3Client.deleteObject(
                            DeleteObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(resume.getS3Key())
                                    .build()
                    );
                    resumeRepository.delete(resume);
                });

        studentFormRepository.deleteById(studentFormId);
    }
}
