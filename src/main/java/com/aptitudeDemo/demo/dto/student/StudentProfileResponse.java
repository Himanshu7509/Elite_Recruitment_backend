package com.aptitudeDemo.demo.dto.student;

import com.aptitudeDemo.demo.model.OpenAI.Questions;
import com.aptitudeDemo.demo.model.student.Feedback;
import com.aptitudeDemo.demo.model.student.Result;
import com.aptitudeDemo.demo.model.student.Resume;
import com.aptitudeDemo.demo.model.student.StudentForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileResponse {

    private StudentForm studentForm;
    private Feedback feedback;
    private Result result;
    private Resume resume;
    private Questions questions;
}

