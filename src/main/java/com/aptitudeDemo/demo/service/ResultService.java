package com.aptitudeDemo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptitudeDemo.demo.dto.student.ResultReq;
import com.aptitudeDemo.demo.model.student.Result;
import com.aptitudeDemo.demo.repository.ResultRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public Result saveResult(String studentFormId, ResultReq resultReq){

        Result result = new Result();
        result.setStudentFormId(studentFormId);
        result.setFullName(resultReq.getFullName());
        result.setCorrectAnswer(resultReq.getCorrectAnswer());
        Result saved = resultRepository.save(result);
        
        log.info("Saved result for studentFormId: {}", studentFormId);

        return saved;
    }
    
    public Result getByStudentFormId(String studentFormId) {
        return resultRepository
                .findByStudentFormId(studentFormId)
                .orElse(null);
    }

    public int correctAnswerByName(String fullName) {

    return resultRepository
            .findByFullName(fullName)
            .map(Result::getCorrectAnswer)
            .orElse(0);
}
    
    public List<Result> getAllResults() {
        log.info("Fetching all results");
        return resultRepository.findAll();
    }
    
    public Optional<Result> getResultById(String id) {
        log.info("Fetching result by ID: {}", id);
        return resultRepository.findById(id);
    }





    
}
