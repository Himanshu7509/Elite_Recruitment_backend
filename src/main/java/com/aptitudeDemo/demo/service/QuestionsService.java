package com.aptitudeDemo.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.QuestionsRequest;
import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.repository.QuestionsRepository;

@Service
public class QuestionsService {
    
    private  QuestionsRepository repository;

    private Questions mapToEntity(QuestionsRequest dto) {
        return new Questions(
                dto.getId(),
                dto.getEmail(),
                dto.getFullName(),
                dto.getQuestions()
        );
    }

    private QuestionsRequest mapToDTO(Questions entity) {
        return new QuestionsRequest(
                entity.getId(),
                entity.getEmail(),
                entity.getFullName(),
                entity.getQuestions()
        );
    }

    
    public QuestionsRequest create(QuestionsRequest request) {
        Questions saved = repository.save(mapToEntity(request));
        return mapToDTO(saved);
    }

    
    public QuestionsRequest update(String id, QuestionsRequest request) {
        Questions existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questions not found"));

        existing.setEmail(request.getEmail());
        existing.setFullName(request.getFullName());
        existing.setQuestions(request.getQuestions());

        return mapToDTO(repository.save(existing));
    }

    
    public void delete(String id) {
        repository.deleteById(id);
    }

    
    public QuestionsRequest getById(String id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Questions not found"));
    }

    
    public List<QuestionsRequest> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    
    public List<QuestionsRequest> getByEmail(String email) {
        return repository.findByEmail(email)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
} 