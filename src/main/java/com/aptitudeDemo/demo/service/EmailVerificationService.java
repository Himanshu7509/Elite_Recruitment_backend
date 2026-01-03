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
public class EmailVerificationService {

    @Autowired
    @Qualifier("brevoWebClient")
    private RestClient brevoWebClient;
    
    @Value("${brevo.sender.email:hello@eliterecruitment.com}")
    private String senderEmail;
    
    @Value("${brevo.sender.name:Elite Recruitment}")
    private String senderName;



    // In-memory storage for verification tokens
    private final Map<String, String> verificationTokens = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenTimestamps = new ConcurrentHashMap<>();

    // Token expiration time in milliseconds (24 hours)
    private static final long TOKEN_EXPIRY_TIME = 24 * 60 * 60 * 1000;

    public boolean sendVerificationEmail(String email) {
    try {
        // Generate OTP
        String verificationCode = generateVerificationCode();
        log.info("Generated verification code: {} for email: {}", verificationCode, email);

        // Store OTP + timestamp
        verificationTokens.put(email, verificationCode);
        tokenTimestamps.put(email, System.currentTimeMillis());

        // Optional: derive user name from email
        String userName = email.split("@")[0];

        String subject = "OTP Verification - Elite Aptitude Test";

        String htmlContent = buildOtpHtmlTemplate(
                userName,
                verificationCode,
                email
        );

        String textContent =
                "Email Verification\n\n" +
                "Your OTP is: " + verificationCode + "\n" +
                "This OTP is valid for 15 minutes.\n\n" +
                "Do not share this code with anyone.";

        // Brevo request body
        Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", new Object[]{Map.of("email", email)},
                "subject", subject,
                "htmlContent", htmlContent,
                "textContent", textContent
        );

        Map<String, Object> response = brevoWebClient.post()
                .uri("/smtp/email")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        log.info("Brevo API response: {}", response);

        return response != null;

    } catch (Exception e) {
        log.error("Error sending verification email to: {}", email, e);
        return false;
    }
}

private String buildOtpHtmlTemplate(String userName, String otp, String email) {

    return """
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>OTP Verification - Elite Apptitude Test</title>
</head>

<body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f5f5f5;">
  <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f5f5f5;">
    <tr>
      <td align="center" style="padding:20px 0;">

        <table width="600" cellpadding="0" cellspacing="0"
          style="background-color:#ffffff; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden;">

          <tr>
            <td style="padding:30px 40px; background:linear-gradient(135deg,#667eea,#764ba2);">
              <h1 style="margin:0; color:#ffffff; text-align:center;">
                Elite Apptitude Test
              </h1>
              <p style="margin:8px 0 0; color:rgba(255,255,255,0.9); text-align:center;">
                Elite Apptitude Test
              </p>
            </td>
          </tr>

          <tr>
            <td style="padding:40px;">
              <h2>Hello %s,</h2>

              <p>
                Thank you for registering with <strong>Elite Apptitude Test</strong>.
                Please verify your email using the OTP below.
              </p>

              <div style="text-align:center; margin:30px 0;">
                <div style="display:inline-block; padding:18px 30px; background:#f8f9fa;
                            border:2px dashed #667eea; border-radius:10px;">
                  <span style="font-size:26px; font-weight:bold; letter-spacing:4px; color:#667eea;">
                    %s
                  </span>
                </div>
              </div>

              <p>
                This OTP is valid for <strong>15 minutes</strong>.
                Do not share it with anyone.
              </p>

              <div style="margin:25px 0; padding:15px; background:#fff3cd;
                          border-left:4px solid #ffc107;">
                <strong>Security Alert</strong><br/>
                We will never ask for your OTP.
              </div>

              <p style="font-size:14px;">
                If you did not request this verification, please ignore this email.
              </p>
            </td>
          </tr>

          <tr>
            <td style="padding:30px; background:#f8f9fa; text-align:center; font-size:13px;">
              © %d Elite Apptitude Test<br/>
              This email was sent to %s
            </td>
          </tr>

        </table>

      </td>
    </tr>
  </table>
</body>
</html>
""".formatted(
            userName,
            otp,
            java.time.Year.now().getValue(),
            email
    );
}


    private String buildTestSubmittedHtml(String name, String email) {
        int year = java.time.Year.now().getValue();
    
        return """
    <!DOCTYPE html>
    <html>
    <head>
    <meta charset="UTF-8">
    <title>Test Submitted - Elite Jobs</title>
    </head>
    
    <body style="margin:0; padding:0; font-family:Arial, sans-serif; background-color:#f5f5f5;">
    <table width="100%%">
    <tr>
    <td align="center" style="padding:20px 0;">
    <table width="600" style="background:#ffffff; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.1);">
    
    <tr>
    <td style="padding:30px 40px; background:linear-gradient(135deg,#667eea,#764ba2);">
    <h1 style="color:#fff; text-align:center;">Elite Jobs</h1>
    <p style="color:#eee; text-align:center;">Job Portal Platform</p>
    </td>
    </tr>
    
    <tr>
    <td style="padding:40px;">
    <h2>Hello %s,</h2>
    
    <p>We’re happy to inform you that your <strong>Aptitude Test</strong> has been
    <strong>successfully submitted</strong>.</p>
    
    <div style="background:#e7f5ff; padding:15px; border-left:4px solid #339af0;">
    <p><strong>✅ Submission Status:</strong> Successful</p>
    </div>
    
    <p>Our team will review your test and notify you about next steps.</p>
    
    <p>Best of luck!</p>
    </td>
    </tr>
    
    <tr>
    <td style="padding:30px; background:#f8f9fa; text-align:center;">
    <p>© %d Elite Jobs. All rights reserved.</p>
    <p>This email was sent to %s</p>
    </td>
    </tr>
    
    </table>
    </td>
    </tr>
    </table>
    </body>
    </html>
    """.formatted(name, year, email);
    }
    

    public boolean sendTestSubmittedEmail(String email, String name) {
        try {
            String subject = "Aptitude Test Submitted - Elite Jobs";
    
            String htmlContent = buildTestSubmittedHtml(name, email);
    
            Map<String, Object> requestBody = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", new Object[]{ Map.of("email", email) },
                "subject", subject,
                "htmlContent", htmlContent
            );
    
            Map<String, Object> response = brevoWebClient.post()
                    .uri("/smtp/email")
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
    
            log.info("Test submission email response: {}", response);
    
            return response != null;
    
        } catch (Exception e) {
            log.error("Failed to send test submission email to {}", email, e);
            return false;
        }
    }
    
    
    
    public boolean verifyCodeOnly(String verificationCode) {
        try {
            // Find the email associated with this verification code
            String email = null;
            for (Map.Entry<String, String> entry : verificationTokens.entrySet()) {
                if (entry.getValue().equals(verificationCode)) {
                    email = entry.getKey();
                    break;
                }
            }
            
            if (email == null) {
                log.warn("No email found for verification code: {}", verificationCode);
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
            boolean isValid = verificationTokens.get(email).equals(verificationCode);
            if (isValid) {
                // Clean up after successful verification
                verificationTokens.remove(email);
                tokenTimestamps.remove(email);
                log.info("Email verified successfully for: {} using code: {}", email, verificationCode);
            } else {
                log.warn("Invalid verification code for email: {}", email);
            }
            
            return isValid;
        } catch (Exception e) {
            log.error("Error verifying code: {}", verificationCode, e);
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