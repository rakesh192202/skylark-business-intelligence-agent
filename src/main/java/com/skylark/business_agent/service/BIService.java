package com.skylark.business_agent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skylark.business_agent.ai.GroqService;
import org.springframework.stereotype.Service;

@Service
public class BIService {

    private final MondayService mondayService;
    private final GroqService groqService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BIService(MondayService mondayService,
                     GroqService groqService) {

        this.mondayService = mondayService;
        this.groqService = groqService;
    }

    public String processQuestion(String question) {

        try {

            String dealsJson = mondayService.getDeals();
            String workOrdersJson = mondayService.getWorkOrders();

            String deals = formatBoardData(dealsJson);
            String workOrders = formatBoardData(workOrdersJson);

            String prompt = """
                    You are an AI Business Intelligence Agent for Skylark Drones.

                    The founder has asked:

                    "%s"

                    ==========================
                    DEALS DATA
                    ==========================

                    %s

                    ==========================
                    WORK ORDERS DATA
                    ==========================

                    %s

                    Instructions:

                    • Answer like a business analyst.
                    • Use both datasets.
                    • Handle missing values gracefully.
                    • Mention any data quality issues.
                    • Give founder-level insights.
                    • Provide:
                      1. Executive Summary
                      2. Risks
                      3. Opportunities
                      4. Recommendations

                    Keep the answer concise and professional.
                    """.formatted(question, deals, workOrders);

            return groqService.askGroq(prompt);

        } catch (Exception e) {
            return "Unable to process the business data. Error: " + e.getMessage();
        }
    }

    private String formatBoardData(String json) throws Exception {

        JsonNode root = objectMapper.readTree(json);

        JsonNode items = root.path("data")
                .path("boards")
                .get(0)
                .path("items_page")
                .path("items");

        StringBuilder builder = new StringBuilder();

        for (JsonNode item : items) {

            builder.append("Item: ")
                    .append(item.path("name").asText("Unknown"))
                    .append("\n");

            for (JsonNode column : item.path("column_values")) {

                String columnName = column.path("id").asText();
                String value = column.path("text").asText();

                if (value == null || value.isBlank() || value.equalsIgnoreCase("null")) {
                    value = "Unknown";
                }

                builder.append(columnName)
                        .append(" : ")
                        .append(value)
                        .append("\n");
            }

            builder.append("---------------------------------\n");
        }

        return builder.toString();
    }
}