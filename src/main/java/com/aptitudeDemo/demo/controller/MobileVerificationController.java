package com.aptitudeDemo.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.MobileVerificationRequest;
import com.aptitudeDemo.demo.service.MobileVerificationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mobile-verification")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class MobileVerificationController {

    @Autowired
    private MobileVerificationService mobileVerificationService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody MobileVerificationRequest request) {
        log.info("Received request to send OTP to mobile: {}", request.getMobileNumber());

        try {
            // Validate mobile number format (basic validation)
            String mobileNumber = request.getMobileNumber();
            if (mobileNumber == null || !isValidMobileNumber(mobileNumber)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid mobile number format"));
            }

            boolean success = mobileVerificationService.sendOtp(mobileNumber);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "OTP sent successfully", "mobileNumber", mobileNumber));
            } else {
                return ResponseEntity.status(500).body(Map.of("error", "Failed to send OTP"));
            }
        } catch (Exception e) {
            log.error("Error sending OTP: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        log.info("Received request to verify OTP");

        try {
            String mobileNumber = request.get("mobileNumber");
            String otp = request.get("otp");

            if (mobileNumber == null || otp == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Mobile number and OTP are required"));
            }

            if (!isValidMobileNumber(mobileNumber)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid mobile number format"));
            }

            boolean isValid = mobileVerificationService.verifyOtp(mobileNumber, otp);
            if (isValid) {
                return ResponseEntity.ok(Map.of("message", "Mobile number verified successfully", "verified", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired OTP"));
            }
        } catch (Exception e) {
            log.error("Error verifying OTP: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    // Basic mobile number validation (10 digits for Indian mobile numbers)
    private boolean isValidMobileNumber(String mobileNumber) {
        // Remove any spaces, hyphens, or other characters
        String cleanedNumber = mobileNumber.replaceAll("[^0-9]", "");
        
        // For Indian mobile numbers, it should be 10 digits
        // If the number starts with country code (+91 or 91), it could be 12 digits
        if (cleanedNumber.startsWith("91") && cleanedNumber.length() == 12) {
            cleanedNumber = cleanedNumber.substring(2); // Remove country code
        } else if (cleanedNumber.startsWith("+91") && cleanedNumber.length() == 13) {
            cleanedNumber = cleanedNumber.substring(3); // Remove country code
        }
        
        // Now check if it's a valid 10-digit Indian mobile number
        // First digit should be between 6-9
        return cleanedNumber.length() == 10 && 
               cleanedNumber.matches("[6-9][0-9]{9}");
    }
}