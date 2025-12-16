package com.aptitudeDemo.demo.model.student;

import lombok.Data;

@Data
public class LanguageAbility {
    private String language;
    private boolean canRead;
    private boolean canWrite;
    private boolean canSpeak;
}