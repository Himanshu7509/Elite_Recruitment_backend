package com.aptitudeDemo.demo.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.dto.JwtResponse;
import com.aptitudeDemo.demo.dto.SignupRequest;
import com.aptitudeDemo.demo.model.User;
import com.aptitudeDemo.demo.security.JwtUtils;
import com.aptitudeDemo.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth/student")
public class AuthController {
    
    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("Received signup request for email: {}", signUpRequest.getEmail());
        
        // Validate email format
        if (!isValidEmail(signUpRequest.getEmail())) {
            log.warn("Invalid email format provided: {}", signUpRequest.getEmail());
            return ResponseEntity.badRequest().body("Error: Invalid email format!");
        }

        // Check if email is already taken
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            log.warn("Email already registered: {}", signUpRequest.getEmail());
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }

        // Create new user's account
        User user = userService.createUser(signUpRequest);
        log.info("Created new user with ID: {}", user.getId());

        // Generate JWT token
        String token = jwtUtils.generateJwtToken(user.getId());
        log.info("Generated JWT token for user ID: {}", user.getId());

        return ResponseEntity.ok(new JwtResponse(token, user.getId()));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}