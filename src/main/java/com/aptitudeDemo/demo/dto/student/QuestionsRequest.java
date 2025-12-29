package com.aptitudeDemo.demo.dto.student;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.aptitudeDemo.demo.model.OpenAI.Question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class QuestionsRequest {
    
    private String email;
    private String fullName;
    private List<Question> questions;
}
