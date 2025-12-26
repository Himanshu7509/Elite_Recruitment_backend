package com.aptitudeDemo.demo.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class MobileVerificationRequest {
    private String mobileNumber;
}