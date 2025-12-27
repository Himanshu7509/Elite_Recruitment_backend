package com.aptitudeDemo.demo.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MobileVerificationService {

    @Autowired
    @Qualifier("fast2SmsWebClient")
    private RestClient fast2SmsWebClient;

    @Value("${fast2sms.sender-id}")
    private String senderId;

    @Value("${fast2sms.route}")
    private String route;

    // In-memory storage for OTPs (will be cleared after verification or timeout)
    // In a real production environment, you might want to use Redis or another cache
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Map<String, Long> otpTimestamp = new ConcurrentHashMap<>();

    // OTP expiration time in milliseconds (5 minutes)
    private static final long OTP_EXPIRY_TIME = 5 * 60 * 1000;

    public boolean sendOtp(String mobileNumber) {
    try {
        String otp = generateOtp();
        log.info("Generated OTP: {} for mobile: {}", otp, mobileNumber);

        otpStore.put(mobileNumber, otp);
        otpTimestamp.put(mobileNumber, System.currentTimeMillis());

        String message = "Your OTP for verification is: " + otp + ". Valid for 5 minutes.";
        
        Map<String, Object> requestBody = Map.of(
                "sender_id", senderId,
                "message", message,
                "route", route,
                "numbers", new String[]{mobileNumber}
        );

        Map response = fast2SmsWebClient.post()
                .uri("/sms")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        log.info("Fast2SMS response: {}", response);

        log.info("Fast2SMS response: {}", response);
        
        // Check the response structure from Fast2SMS API
        if (response != null && response.containsKey("return")) {
            Object returnObj = response.get("return");
            if (returnObj instanceof Boolean) {
                return (Boolean) returnObj;
            } else if (returnObj instanceof String) {
                return "true".equalsIgnoreCase((String) returnObj);
            }
            // For numeric responses (0/1)
            else if (returnObj instanceof Number) {
                return ((Number) returnObj).intValue() == 1;
            }
        }
        
        // Log the specific error if available
        if (response != null && response.containsKey("message")) {
            log.error("Fast2SMS API error message: {}", response.get("message"));
        }
        
        return false;
        
    } catch (Exception e) {
        log.error("Error sending OTP", e);
        return false;
    }
}



    public boolean verifyOtp(String mobileNumber, String otp) {
        try {
            // Check if OTP exists for this mobile number
            String storedOtp = otpStore.get(mobileNumber);
            if (storedOtp == null) {
                log.warn("No OTP found for mobile: {}", mobileNumber);
                return false;
            }

            // Check if OTP has expired
            Long timestamp = otpTimestamp.get(mobileNumber);
            if (timestamp != null && System.currentTimeMillis() - timestamp > OTP_EXPIRY_TIME) {
                log.warn("OTP expired for mobile: {}", mobileNumber);
                // Clean up expired OTP
                otpStore.remove(mobileNumber);
                otpTimestamp.remove(mobileNumber);
                return false;
            }

            // Verify OTP
            boolean isValid = storedOtp.equals(otp);
            if (isValid) {
                // Clean up after successful verification
                otpStore.remove(mobileNumber);
                otpTimestamp.remove(mobileNumber);
                log.info("OTP verified successfully for mobile: {}", mobileNumber);
            } else {
                log.warn("Invalid OTP for mobile: {} - provided: {}, stored: {}", mobileNumber, otp, storedOtp);
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying OTP for mobile: {}", mobileNumber, e);
            return false;
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

    public boolean isOtpExpired(String mobileNumber) {
        Long timestamp = otpTimestamp.get(mobileNumber);
        if (timestamp != null) {
            return System.currentTimeMillis() - timestamp > OTP_EXPIRY_TIME;
        }
        return true;
    }

    // Clean up expired OTPs periodically (you might want to schedule this)
    public void cleanupExpiredOtps() {
        long currentTime = System.currentTimeMillis();
        otpStore.entrySet().removeIf(entry -> {
            String mobile = entry.getKey();
            Long timestamp = otpTimestamp.get(mobile);
            if (timestamp != null && currentTime - timestamp > OTP_EXPIRY_TIME) {
                otpTimestamp.remove(mobile);
                return true;
            }
            return false;
        });
    }
}