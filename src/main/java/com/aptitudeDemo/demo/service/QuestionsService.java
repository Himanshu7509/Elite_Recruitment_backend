package com.aptitudeDemo.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.QuestionsRequest;
import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.repository.QuestionsRepository;

@Service
public class QuestionsService {
    private QuestionsRepository repository;

    
    public Questions create(QuestionsRequest request) {
        Questions entity = new Questions(
                null, // MongoDB generates ID
                request.getEmail(),
                request.getFullName(),
                request.getQuestions()
        );
        return repository.save(entity);
    }
    public Questions update(String id, QuestionsRequest request) {
        Questions existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questions not found"));

        existing.setEmail(request.getEmail());
        existing.setFullName(request.getFullName());
        existing.setQuestions(request.getQuestions());

        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Questions getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questions not found"));
    }

    public List<Questions> getAll() {
        return repository.findAll();
    }

    public List<Questions> getByEmail(String email) {
        return repository.findByEmail(email);
    }
}
