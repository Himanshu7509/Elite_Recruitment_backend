package com.aptitudeDemo.demo.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailVerificationService {

    @Autowired
    @Qualifier("brevoWebClient")
    private RestClient brevoWebClient;



    // In-memory storage for verification tokens
    private final Map<String, String> verificationTokens = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenTimestamps = new ConcurrentHashMap<>();

    // Token expiration time in milliseconds (24 hours)
    private static final long TOKEN_EXPIRY_TIME = 24 * 60 * 60 * 1000;

    public boolean sendVerificationEmail(String email) {
        try {
            // Generate a 6-digit verification code
            String verificationCode = generateVerificationCode();
            log.info("Generated verification code: {} for email: {}", verificationCode, email);

            // Store the verification code with timestamp
            verificationTokens.put(email, verificationCode);
            tokenTimestamps.put(email, System.currentTimeMillis());

            // Create email content
            String subject = "Email Verification";
            String textContent = "Email Verification\n\n" +
                    "Thank you for registering. Please use the following verification code:\n\n" +
                    verificationCode + "\n\n" +
                    "This code will expire in 24 hours.\n\n" +
                    "If you didn't request this, please ignore this email.";

            // Prepare the request body for Brevo
            Map<String, Object> emailData = Map.of(
                    "sender", Map.of("email", "noreply@yourdomain.com", "name", "Elite Recruitment"),
                    "to", new Object[]{Map.of("email", email)},
                    "subject", subject,
                    "textContent", textContent
            );

            Map<String, Object> requestBody = Map.of(
                    "email", emailData
            );

            // Send the email via Brevo
            Map<String, Object> response = brevoWebClient.post()
                    .uri("/smtp/email")
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            log.info("Brevo API response: {}", response);

            // Check if the email was sent successfully
            return response != null;
        } catch (Exception e) {
            log.error("Error sending verification email to: {}", email, e);
            return false;
        }
    }

    public boolean verifyEmail(String email, String verificationCode) {
        try {
            // Check if a verification token exists for this email
            String storedCode = verificationTokens.get(email);
            if (storedCode == null) {
                log.warn("No verification code found for email: {}", email);
                return false;
            }

            // Check if the token has expired
            Long timestamp = tokenTimestamps.get(email);
            if (timestamp != null && System.currentTimeMillis() - timestamp > TOKEN_EXPIRY_TIME) {
                log.warn("Verification code expired for email: {}", email);
                // Clean up expired token
                verificationTokens.remove(email);
                tokenTimestamps.remove(email);
                return false;
            }

            // Verify the code
            boolean isValid = storedCode.equals(verificationCode);
            if (isValid) {
                // Clean up after successful verification
                verificationTokens.remove(email);
                tokenTimestamps.remove(email);
                log.info("Email verified successfully for: {}", email);
            } else {
                log.warn("Invalid verification code for email: {} - provided: {}, stored: {}", 
                        email, verificationCode, storedCode);
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying email: {}", email, e);
            return false;
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate 6-digit code
        return String.valueOf(code);
    }
    

    public boolean isTokenExpired(String email) {
        Long timestamp = tokenTimestamps.get(email);
        if (timestamp != null) {
            return System.currentTimeMillis() - timestamp > TOKEN_EXPIRY_TIME;
        }
        return true;
    }

    // Clean up expired tokens periodically
    public void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        verificationTokens.entrySet().removeIf(entry -> {
            String email = entry.getKey();
            Long timestamp = tokenTimestamps.get(email);
            if (timestamp != null && currentTime - timestamp > TOKEN_EXPIRY_TIME) {
                tokenTimestamps.remove(email);
                return true;
            }
            return false;
        });
    }
}