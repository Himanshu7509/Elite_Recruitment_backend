package com.aptitudeDemo.demo.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSubmitRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;
}