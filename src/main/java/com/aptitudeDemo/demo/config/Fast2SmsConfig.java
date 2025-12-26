package com.aptitudeDemo.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Fast2SmsConfig {

    @Value("${fast2sms.api-key}")
    private String apiKey;

    @Bean
    public RestClient fast2SmsWebClient() {
        return RestClient.builder()
                .baseUrl("https://www.fast2sms.com/dev")
                .defaultHeader("authorization", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}