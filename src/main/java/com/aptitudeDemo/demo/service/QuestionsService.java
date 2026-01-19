package com.aptitudeDemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.QuestionsRequest;
import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.repository.QuestionsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QuestionsService {
    
    @Autowired
    private QuestionsRepository repository;

    
    public Questions create(String studentFormId, QuestionsRequest request){
    	
    	// Validate that studentFormId is a proper MongoDB ObjectId format
    	if (!isValidMongoId(studentFormId)) {
    	    log.warn("Invalid studentFormId format detected: {}. Expected MongoDB ObjectId format.", studentFormId);
    	}
    	
    	repository.findByStudentFormId(studentFormId)
        .ifPresent(existing -> repository.delete(existing));
    	
        Questions entity = new Questions();
        		entity.setStudentFormId(studentFormId);
        		entity.setEmail(request.getEmail());
        		entity.setFullName(request.getFullName());
        		entity.setQuestions(request.getQuestions());
        		
        		Questions saved = repository.save(entity);
        		log.info("Questions saved with studentFormId: {}", saved.getStudentFormId());
        		return saved;
        
    }
    
    private boolean isValidMongoId(String id) {
        // MongoDB ObjectId is 24 hexadecimal characters
        return id != null && id.matches("^[0-9a-fA-F]{24}$");
    }
    
    public Questions updateByStudentFormId(String studentFormId, QuestionsRequest request) {
        Questions existing = repository.findByStudentFormId(studentFormId)
                .orElseThrow(() -> new RuntimeException("Questions not found"));

        existing.setEmail(request.getEmail());
        existing.setFullName(request.getFullName());
        existing.setQuestions(request.getQuestions());

        return repository.save(existing);
    }

    public Questions getByStudentFormId(String studentFormId) {
        return repository.findByStudentFormId(studentFormId).orElse(null);
    }

    public List<Questions> getAll() {
        return repository.findAll();
    }

    public List<Questions> getByEmail(String email) {
        return repository.findByEmail(email);
    }
}
