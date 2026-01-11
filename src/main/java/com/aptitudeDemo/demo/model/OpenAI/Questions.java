package com.aptitudeDemo.demo.model.OpenAI;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ai_questions")
@Data @AllArgsConstructor @NoArgsConstructor
public class Questions {
    
    
    @Id
    private String id;

    @Indexed
    private String studentFormId;
    
    private String email;
    private String fullName;
    private List<Question> questions;
}
