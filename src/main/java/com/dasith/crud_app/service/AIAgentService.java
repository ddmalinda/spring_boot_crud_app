package com.dasith.crud_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AIAgentService {
    private final WebClient webClient;
    private final String geminiApiUrl;
    private final String geminiApiKey;
    private final String customPrompt;

    public AIAgentService(WebClient.Builder webClientBuilder,
                          @Value("${gemini.api.url}") String geminiApiUrl,
                          @Value("${gemini.api.key}") String geminiApiKey,
                          @Value("${app.gemini.custom-prompt}") String customPrompt) {
        this.webClient = webClientBuilder.build();
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey;
        this.customPrompt = customPrompt;
    }

    public Mono<String> getGeminiResponse(String userPrompt) {
        // Combine your predefined custom prompt with the user's input
        String finalPrompt = customPrompt + "\n\nUser: " + userPrompt + "\nAI:";

        // Construct the request body for the Gemini API
        String requestBody = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", finalPrompt
        );

        return webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractContentFromJson); // Helper to parse the response
    }

    // A simple helper method to extract the text content from Gemini's JSON response
    private String extractContentFromJson(String jsonResponse) {
        try {
            // This is a very basic parser. For production, use a library like Jackson or Gson.
            int start = jsonResponse.indexOf("\"text\": \"") + 9;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end).replace("\\n", "\n");
        } catch (Exception e) {
            return "Error parsing response from AI.";
        }
    }
}
