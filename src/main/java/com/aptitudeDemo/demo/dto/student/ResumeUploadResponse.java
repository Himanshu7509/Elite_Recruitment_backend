package com.aptitudeDemo.demo.dto.student;


public record ResumeUploadResponse(
        String s3Key,
        String resumeUrl
) {}
