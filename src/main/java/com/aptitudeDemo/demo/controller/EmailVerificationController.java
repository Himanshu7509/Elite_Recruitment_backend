package com.aptitudeDemo.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.EmailSubmitRequest;
import com.aptitudeDemo.demo.dto.student.EmailVerificationRequest;
import com.aptitudeDemo.demo.service.EmailVerificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/email-verification")
@Slf4j
public class EmailVerificationController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody EmailVerificationRequest request) {
        log.info("Received request to send verification email to: {}", request.getEmail());

        try {
            String email = request.getEmail();
            if (email == null || !isValidEmail(email)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email format"));
            }

            boolean success = emailVerificationService.sendVerificationEmail(email);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Verification email sent successfully", "email", email));
            } else {
                return ResponseEntity.status(500).body(Map.of("error", "Failed to send verification email - please check your API configuration"));
            }
        } catch (Exception e) {
            log.error("Error sending verification email: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/submit")
public ResponseEntity<?> sendTestSubmittedMail(
        @RequestBody EmailSubmitRequest request) {

    log.info("Sending test submission email to {}", request.getEmail());

    if (request.getEmail() == null || request.getName() == null) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Email and name are required"));
    }

    boolean sent = emailVerificationService
            .sendTestSubmittedEmail(request.getEmail(), request.getName());

    if (sent) {
        return ResponseEntity.ok(
                Map.of("message", "Test submission email sent successfully"));
    } else {
        return ResponseEntity.status(500)
                .body(Map.of("error", "Failed to send email"));
    }
}

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        log.info("Received request to verify email");

        try {
            String verificationCode = request.get("verificationCode");

            if (verificationCode == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Verification code is required"));
            }

            boolean isValid = emailVerificationService.verifyCodeOnly(verificationCode);
            if (isValid) {
                return ResponseEntity.ok(Map.of("message", "Email verified successfully", "verified", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired verification code"));
            }
        } catch (Exception e) {
            log.error("Error verifying email: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
}