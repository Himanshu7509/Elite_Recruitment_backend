package com.aptitudeDemo.demo.dto.student;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class FeedbackReq{

    private int rating;
    private String name;
    private String problem1;
    private String problem2;
    private String problem3;
    private String problem4;
    private String problem5;
   
}
