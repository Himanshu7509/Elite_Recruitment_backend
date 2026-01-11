package com.aptitudeDemo.demo.model.student;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "resumes")
public class Resume {

    @Id
    private String id;

    private String studentFormId;
  
    private String s3Key;     
    private String resumeUrl;
    private String email;

    private LocalDateTime uploadedAt;
}
