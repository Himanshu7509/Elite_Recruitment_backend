package com.aptitudeDemo.demo.controller.openai;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptitudeDemo.demo.model.OpenAI.TestRequest;
import com.aptitudeDemo.demo.service.OpenAiService;

@RestController
@RequestMapping("/open")
public class TestGenerationController {

    private final OpenAiService openAiService;

    public TestGenerationController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/generate")
    public String generateTest(@RequestBody TestRequest request) throws Exception {
        return openAiService.generateTest(request);
    }
}

