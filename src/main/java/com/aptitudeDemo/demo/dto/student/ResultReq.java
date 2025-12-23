package com.aptitudeDemo.demo.dto.student;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultReq {

    @Id
    private String id;

    private String fullName;
    private int correctAnswer;

    
}
