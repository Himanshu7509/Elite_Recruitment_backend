package com.aptitudeDemo.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aptitudeDemo.demo.model.student.Resume;
import com.aptitudeDemo.demo.repository.ResumeRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
public class ResumeS3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private ResumeRepository resumeRepository;

    private static final String RESUME_FOLDER = "AptitudeTest/resume/";
    
    public void uploadAndSaveResume(
            MultipartFile file,
            String studentFormId
    ) throws IOException {
    	
    	validateStudentFormId(studentFormId);
        validateFile(file);

        resumeRepository.findByStudentFormId(studentFormId).ifPresent(existing -> {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(existing.getS3Key())
                            .build()
            );
            resumeRepository.delete(existing);
        });

        
        String s3Key = RESUME_FOLDER
                + studentFormId + "-"
                + UUID.randomUUID()
                + "-" + file.getOriginalFilename();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        String resumeUrl =
                "https://" + bucketName
                + ".s3." + s3Client.serviceClientConfiguration().region().id()
                + ".amazonaws.com/" + s3Key;

        Resume resume = new Resume();
        resume.setStudentFormId(studentFormId);
        resume.setS3Key(s3Key);
        resume.setResumeUrl(resumeUrl);
        resume.setUploadedAt(LocalDateTime.now());
        
        resumeRepository.save(resume);

    }
    
    public Resume getResumeByStudentFormId(String studentFormId) {
        return resumeRepository.findByStudentFormId(studentFormId)
                .orElse(null);
    }
    
    private void validateStudentFormId(String studentFormId) {
        if (studentFormId == null || studentFormId.isBlank()) {
            throw new RuntimeException("studentFormId cannot be empty");
        }
    }


    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Resume file is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Max file size is 5MB");
        }

        if (!List.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ).contains(file.getContentType())) {
            throw new RuntimeException("Only PDF, DOC, DOCX allowed");
        }
    }

}
