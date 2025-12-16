package com.aptitudeDemo.demo.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String id;
    

    public JwtResponse(String accessToken, String id) {
        this.token = accessToken;
        this.id = id;
        
    }
}