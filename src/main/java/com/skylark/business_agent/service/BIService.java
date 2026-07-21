package com.skylark.business_agent.service;

import com.skylark.business_agent.ai.GroqService;
import com.skylark.business_agent.model.Deal;
import com.skylark.business_agent.model.WorkOrder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BIService {

    private final MondayService mondayService;
    private final GroqService groqService;
    private final BusinessAnalysisService analysisService;

    public BIService(MondayService mondayService,
                     GroqService groqService,
                     BusinessAnalysisService analysisService) {

        this.mondayService = mondayService;
        this.groqService = groqService;
        this.analysisService = analysisService;
    }

    public String processQuestion(String question) {

        try {

            // Fetch live data from Monday.com
            List<Deal> deals = mondayService.getDealsData();
            List<WorkOrder> workOrders = mondayService.getWorkOrdersData();

            // Analyze business data
            Map<String, Object> summary =
                    analysisService.analyze(deals, workOrders);

            String prompt = """
                    You are the Business Intelligence Agent for Skylark Drones.

                    Business Summary:

                    %s

                    Founder Question:

                    "%s"

                    Instructions:

                    • Answer like an experienced business analyst.
                    • Use the business summary provided.
                    • Mention any data quality issues.
                    • Give founder-level insights.
                    • Be concise and professional.

                    Response Format:

                    1. Executive Summary
                    2. Risks
                    3. Opportunities
                    4. Recommendations
                    """
                    .formatted(summary, question);

            return groqService.askGroq(prompt);

        } catch (Exception e) {

            e.printStackTrace();

            return "Unable to process business data.\n\n"
                    + e.getMessage();
        }
    }
}