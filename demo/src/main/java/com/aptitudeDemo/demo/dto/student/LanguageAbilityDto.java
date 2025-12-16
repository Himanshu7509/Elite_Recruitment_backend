package com.aptitudeDemo.demo.dto.student;

import lombok.Data;

@Data
public class LanguageAbilityDto {
    private String language;
    private boolean canRead;
    private boolean canWrite;
    private boolean canSpeak;
}