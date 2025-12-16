package com.aptitudeDemo.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.SignupRequest;
import com.aptitudeDemo.demo.model.User;
import com.aptitudeDemo.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    

    @Autowired
    UserRepository userRepository;

    public User createUser(SignupRequest signupRequest) {
        log.info("Creating user with email: {}", signupRequest.getEmail());
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return savedUser;
    }

    public Boolean existsByEmail(String email) {
        log.debug("Checking if user exists with email: {}", email);
        boolean exists = userRepository.existsByEmail(email);
        log.debug("User exists with email {}: {}", email, exists);
        return exists;
    }

    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        log.debug("Found user with email {}: {}", email, user.isPresent());
        return user;
    }
}