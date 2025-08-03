package com.dasith.crud_app.service;

import com.dasith.crud_app.model.Business;
import com.dasith.crud_app.model.Product;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class AIAgentService {
    private final BusinessService businessService;
    private final ProductService productService;
    private final Client geminiClient;
    private final Map<String, String> responseCache = new ConcurrentHashMap<>();

    public AIAgentService(BusinessService businessService, ProductService productService,@Value("${gemini.api.key}") String apiKey ) {
        this.businessService = businessService;
        this.productService = productService;
        this.geminiClient = Client.builder().apiKey(apiKey).build();
    }

    public Mono<String> getGeminiResponse(String userPrompt, Long businessId) {
        // Generate cache key
        String cacheKey = businessId + ":" + userPrompt.trim();

        // Check cache first
        if (responseCache.containsKey(cacheKey)) {
            return Mono.just(responseCache.get(cacheKey));
        }

        return Mono.fromCallable(() -> {
            Business businessDetails = businessService.getBusinessById(businessId)
                    .orElseThrow(() -> new RuntimeException("Business not found with id " + businessId));

            List<Product> productList = productService.getProductsByBusinessId(businessId);
            String formattedProductsList = formatProductsList(productList);

            String businessSpecificPrompt = "You are a friendly, enthusiastic, and knowledgeable AI assistant for " +
                    businessDetails.getName() + ". Your primary goal is to provide outstanding customer service by answering questions accurately and helping customers find the perfect products to meet their needs. " +
                    "Business description: " + businessDetails.getDescription() + " " +
                    "Business industry: " + businessDetails.getIndustry() + " " +
                    "Business Type: " + businessDetails.getType();

            String finalPrompt = businessSpecificPrompt + "\nProducts details:\n" +
                    formattedProductsList + "\n\nUser: " + userPrompt + "\nAI:";

            try {
                GenerateContentResponse response = geminiClient.models.generateContent(
                        "gemini-2.5-flash",
                        finalPrompt,
                        null);

                String result = response.text();
                responseCache.put(cacheKey, result);
                return result;
            } catch (Exception e) {
                return "I'm currently experiencing high demand. Please try again in a few moments.";
            }
        });
    }

    private String formatProductsList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "No product or service available.";
        }

        StringBuilder builder = new StringBuilder();
        for (Product product : products) {
            builder.append("- ")
                    .append("Product Name: ").append(product.getName()).append(", ")
                    .append("Description: ").append(product.getDescription()).append(", ")
                    .append("Price: ").append(product.getPrice()).append(", ")
                    .append("Category: ").append(product.getCategory()).append(", ")
                    .append("Stock: ").append(product.getStock())
                    .append("\n");
        }
        return builder.toString();
    }
}