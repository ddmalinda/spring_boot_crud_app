package com.dasith.crud_app.service;

import com.dasith.crud_app.model.Business;
import com.dasith.crud_app.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AIAgentService {
    private final BusinessService businessService;
    private final ProductService productService;

    private final WebClient webClient;
    private final String geminiApiUrl;
    private final String geminiApiKey;
    private final String customPrompt;

    public AIAgentService(WebClient.Builder webClientBuilder,
                          @Value("${gemini.api.url}") String geminiApiUrl,
                          @Value("${gemini.api.key}") String geminiApiKey,
                          @Value("${app.gemini.custom-prompt}") String customPrompt,
                          BusinessService businessService,
                          ProductService productService) {
        this.webClient = webClientBuilder.build();
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey;
        this.customPrompt = customPrompt;
        this.businessService = businessService;
        this.productService=productService;
    }

    public Mono<String> getGeminiResponse(String userPrompt,Long businessId) {
        Business businessDetails = businessService.getBusinessById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id " + businessId));

       List<Product> productList =productService.getProductsByBusinessId(businessId);

        // Format products list safely to avoid StackOverflowError
        String formattedProductsList = formatProductsList(productList);


        String businessSpecificPrompt = "You are a friendly, enthusiastic, and knowledgeable AI assistant for " +
                businessDetails.getName() + ". Your primary goal is to provide outstanding customer service by answering questions accurately and helping customers find the perfect products to meet their needs. " +
                "Business description: " + businessDetails.getDescription()+
                "Business industry"+businessDetails.getIndustry()+
                "Business Type"+ businessDetails.getType();



        // Combine your predefined custom prompt with the user's input
        String finalPrompt = businessSpecificPrompt+"\nProducts details\n"+ formattedProductsList +"\n\nUser: " + userPrompt + "\nAI:";

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

    private String formatProductsList(List<Product> products){
        if(products==null ||products.isEmpty()){
            return "No product or service avalable.";
        }

        StringBuilder builder =new StringBuilder();
        for(Product product:products){
            builder.append("- ")
                    .append("Product Name:").append(product.getName()).append(", ")
                    .append("Description: ").append(product.getDescription()).append(", ")
                    .append("Price: ").append(product.getPrice()).append(", ")
                    .append("Category: ").append(product.getCategory()).append(", ")
                    .append("Stock: ").append(product.getStock());
        }
        return builder.toString();
    }
}
