package com.aptitudeDemo.demo.model.student;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resumes")
public class Resume {

    @Id
    private String id;
  
    private String s3Key;     
    private String resumeUrl; 

    private LocalDateTime uploadedAt;
}
