package com.aptitudeDemo.demo.dto.student;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class QuestionsRequest {
    @Id
    String id;
    private String email;
    private String fullName;
    private String aiQuestion;
    private List<String> Options;
    private String aiAnswer;
    private String userAnswer;
}
