package com.aptitudeDemo.demo.model.student;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Document(collection = "feedback")
public class Feedback {

    @Id
    private String id;

    
    private String studentFormId;

    private int rating;
    private String name;
    private String problem1;
    private String problem2;
    private String problem3;
    private String problem4;
    private String problem5;
    
}
