package com.dasith.crud_app.controller;

import com.dasith.crud_app.service.AIAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/aiagent/{businessId}")
@CrossOrigin(origins = "*")
public class AIAgentController {
    @Autowired
    private final AIAgentService geminiService;

    public AIAgentController(AIAgentService geminiService) {
        this.geminiService = geminiService;
    }

    // DTO for incoming user request
    public record UserRequest(String prompt) {}

    @PostMapping("/generate")
    public Mono<String> generateContent(@RequestBody UserRequest userRequest,@PathVariable Long businessId) {
        return geminiService.getGeminiResponse(userRequest.prompt(),businessId);
    }
}
