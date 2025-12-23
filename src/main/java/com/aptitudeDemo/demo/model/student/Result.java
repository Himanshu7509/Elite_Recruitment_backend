package com.aptitudeDemo.demo.model.student;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="Result")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    @Id
    private String id;
    
    private String fullName;
    private int correctAnswer;

}
