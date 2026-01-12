package com.aptitudeDemo.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private static final String RESUME_FOLDER = "AptitudeTest/resume";
    
    public void uploadAndSaveResume(
            MultipartFile file,
            String email,
            String studentFormId
    ) throws IOException {

        validateEmail(email);
        validateFile(file);

        resumeRepository.findByEmail(email).ifPresent(existingResume -> {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(existingResume.getS3Key())
                            .build()
            );
            resumeRepository.delete(existingResume);
        });

        String safeEmail=email.replace("@","_").replace(".","_");

        String s3Key = RESUME_FOLDER
                + safeEmail + "-"
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
        resume.setEmail(email);
        
        resumeRepository.save(resume);

    }

    public Resume getResumeByStudentFormId(String studentFormId) {
        return resumeRepository.findByStudentFormId(studentFormId)
                .orElse(null);
    }
    
    private void validateEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email cannot be empty");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (!email.matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
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

    public Resume getResumeByEmail(String email) {

    validateEmail(email);

    return resumeRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("Resume not found for email: " + email));
    }

    public void deleteResumeByEmail(String email) {

    validateEmail(email);

    Resume resume = resumeRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("Resume not found for email: " + email));

    s3Client.deleteObject(
            DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(resume.getS3Key())
                    .build()
    );

    resumeRepository.delete(resume);
}
    
    public List<Resume> getAllResumes() {
    return resumeRepository.findAll();
}


}
