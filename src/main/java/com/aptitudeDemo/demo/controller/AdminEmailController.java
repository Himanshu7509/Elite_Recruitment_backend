package com.aptitudeDemo.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.student.SingleEmailRequest;
import com.aptitudeDemo.demo.service.SendGridEmailService;

@RestController
@RequestMapping("/admin/email")
public class AdminEmailController {

    private final SendGridEmailService emailService;

    public AdminEmailController(SendGridEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestBody SingleEmailRequest request) {

        emailService.sendEmail(
                request.getTo(),
                request.getSubject(),
                request.getMessage()
        );

        return ResponseEntity.ok("Email sent successfully");
    }
}
