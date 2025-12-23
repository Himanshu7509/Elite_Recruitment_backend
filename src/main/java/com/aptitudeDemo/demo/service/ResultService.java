package com.aptitudeDemo.demo.service;

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

    public Result saveResult(ResultReq resultReq){

        Result result = new Result();
        result.setFullName(resultReq.getFullName());
        result.setCorrectAnswer(resultReq.getCorrectAnswer());
        return resultRepository.save(result);
    }

    public int correctAnswerByName(String fullName) {

    return resultRepository
            .findByFullName(fullName)
            .map(Result::getCorrectAnswer)
            .orElse(0);   // return 0 if student not found
}





    
}
