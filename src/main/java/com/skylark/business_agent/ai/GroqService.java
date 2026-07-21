package com.skylark.business_agent.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GroqService {

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GroqService(@Value("${groq.api.key}") String apiKey) {

        this.apiKey = apiKey;

        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String askGroq(String prompt) {

        try {

            String body = """
            {
              "model":"llama-3.3-70b-versatile",
              "messages":[
                {
                  "role":"user",
                  "content":"%s"
                }
              ]
            }
            """.formatted(prompt.replace("\"", "\\\\\""));

            String response = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {
            return "Groq API Error:\n" + e.getMessage();
        }
    }
}