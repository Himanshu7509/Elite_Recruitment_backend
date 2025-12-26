package com.aptitudeDemo.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Fast2SmsConfig {

   @Bean(name = "fast2SmsWebClient")
public RestClient fast2SmsWebClient(
        @Value("${fast2sms.api.key}") String apiKey) {

    return RestClient.builder()
            .baseUrl("https://www.fast2sms.com/dev/bulkV2")
            .defaultHeader("authorization", apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
}
}