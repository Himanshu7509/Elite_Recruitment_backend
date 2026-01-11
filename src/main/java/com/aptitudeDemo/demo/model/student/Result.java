package com.aptitudeDemo.demo.model.student;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Result")
public class Result {

    @Id
    private String id;

    private String studentFormId;
    
    private String fullName;
    private int correctAnswer;

}
