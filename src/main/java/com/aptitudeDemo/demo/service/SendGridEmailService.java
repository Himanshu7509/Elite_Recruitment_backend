package com.aptitudeDemo.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class SendGridEmailService {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String message) {
        send(to, subject, message, "text/plain");
    }

    public void sendOtpEmail(String to, String userName, String otp) {

        String subject = "OTP Verification - Elite Aptitude Test";
        String htmlContent = buildOtpHtmlTemplate(userName, otp, to);

        send(to, subject, htmlContent, "text/html");
    }

    private void send(String to, String subject, String contentValue, String contentType) {

        try {
            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);
            Content content = new Content(contentType, contentValue);

            Mail mail = new Mail(from, subject, toEmail, content);

            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid error: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * HTML TEMPLATE
     */
    private String buildOtpHtmlTemplate(String userName, String otp, String email) {

        return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <title>OTP Verification - Elite Aptitude Test</title>
        </head>

        <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f5f5f5;">
          <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
              <td align="center" style="padding:20px 0;">

                <table width="600" style="background-color:#ffffff; border-radius:8px;">
                  <tr>
                    <td style="padding:30px; background:linear-gradient(135deg,#667eea,#764ba2); text-align:center;">
                      <h1 style="color:#ffffff; margin:0;">Elite Aptitude Test</h1>
                    </td>
                  </tr>

                  <tr>
                    <td style="padding:40px;">
                      <h2>Hello %s,</h2>

                      <p>
                        Thank you for registering with <strong>Elite Aptitude Test</strong>.
                        Please verify your email address using the OTP below.
                      </p>

                      <div style="text-align:center; margin:30px 0;">
                        <div style="display:inline-block; padding:18px 30px; border:2px dashed #667eea;">
                          <span style="font-size:26px; font-weight:bold; letter-spacing:4px; color:#667eea;">
                            %s
                          </span>
                        </div>
                      </div>

                      <p>
                        This OTP is valid for <strong>15 minutes</strong>.
                        Do not share this code with anyone.
                      </p>

                      <p style="font-size:13px; color:#777;">
                        If you did not request this verification, please ignore this email.
                      </p>
                    </td>
                  </tr>

                  <tr>
                    <td style="padding:20px; text-align:center; background:#f8f9fa; font-size:13px;">
                      © %d Elite Aptitude Test<br/>
                      This email was sent to %s
                    </td>
                  </tr>
                </table>

              </td>
            </tr>
          </table>
        </body>
        </html>
        """.formatted(userName, otp, java.time.Year.now().getValue(), email);
    }
}
