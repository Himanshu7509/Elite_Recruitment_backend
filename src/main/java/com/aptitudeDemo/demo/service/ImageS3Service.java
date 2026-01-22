package com.aptitudeDemo.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptitudeDemo.demo.model.student.Image;
import com.aptitudeDemo.demo.model.student.StudentForm;
import com.aptitudeDemo.demo.repository.ImageRepository;
import com.aptitudeDemo.demo.repository.StudentFormRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class ImageS3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private StudentFormRepository studentFormRepository;

    private static final String IMAGE_FOLDER = "AptitudeTest/images/";
    
    public void uploadAndSaveImage(
            MultipartFile file,
            String studentFormId
    ) throws IOException {
    	
    	validateStudentFormId(studentFormId);
        validateFile(file);

        // Delete existing image if exists
        imageRepository.findByStudentFormId(studentFormId).ifPresent(existing -> {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(existing.getS3Key())
                            .build()
            );
            imageRepository.delete(existing);
        });

        
        String s3Key = IMAGE_FOLDER
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

        String imageUrl =
                "https://" + bucketName
                + ".s3." + s3Client.serviceClientConfiguration().region().id()
                + ".amazonaws.com/" + s3Key;

        Image image = new Image();
        image.setStudentFormId(studentFormId);
        image.setS3Key(s3Key);
        image.setImageUrl(imageUrl);
        image.setUploadedAt(LocalDateTime.now());
        
        imageRepository.save(image);
        
        // Update the student form with image information
        studentFormRepository.findById(studentFormId).ifPresent(studentForm -> {
            studentForm.setImageId(image.getId());
            studentForm.setImageUrl(image.getImageUrl());
            studentFormRepository.save(studentForm);
        });

    }
    
    public void uploadAndSaveImageByEmail(MultipartFile file, String email) throws IOException {
        // Find the actual student form ID by email
        Optional<StudentForm> studentOpt = studentFormRepository.findByPermanentEmail(email.toLowerCase().trim());
        
        if (!studentOpt.isPresent()) {
            throw new RuntimeException("Student with email " + email + " not found");
        }
        
        StudentForm student = studentOpt.get();
        String studentFormId = student.getId();
        
        // Now upload using the actual student form ID
        uploadAndSaveImage(file, studentFormId);
    }
    
    public Image getImageByStudentFormId(String studentFormId) {
        return imageRepository.findByStudentFormId(studentFormId)
                .orElse(null);
    }
    
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
    
    private void validateStudentFormId(String studentFormId) {
        if (studentFormId == null || studentFormId.isBlank()) {
            throw new RuntimeException("studentFormId cannot be empty");
        }
    }


    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Max file size is 5MB");
        }

        if (!List.of(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/webp"
        ).contains(file.getContentType())) {
            throw new RuntimeException("Only JPG, PNG, GIF, WEBP images allowed");
        }
    }

}