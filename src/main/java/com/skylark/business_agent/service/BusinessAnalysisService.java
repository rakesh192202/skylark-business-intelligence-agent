package com.skylark.business_agent.service;

import com.skylark.business_agent.model.Deal;
import com.skylark.business_agent.model.WorkOrder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessAnalysisService {

    public Map<String, Object> analyze(List<Deal> deals,
                                       List<WorkOrder> workOrders) {

        Map<String, Object> summary = new HashMap<>();

        summary.put("Total Deals", deals.size());
        summary.put("Total Work Orders", workOrders.size());

        int openDeals = 0;
        int onHoldDeals = 0;
        int closedDeals = 0;
        int highProbability = 0;
        int mediumProbability = 0;
        int lowProbability = 0;

        double totalPipeline = 0;

        Map<String, Integer> ownerWiseDeals = new HashMap<>();
        Map<String, Integer> sectorWiseDeals = new HashMap<>();

        int missingDealFields = 0;

        for (Deal deal : deals) {

            Map<String, String> fields = deal.getFields();

            for (String value : fields.values()) {
                if (value == null ||
                        value.isBlank() ||
                        value.equalsIgnoreCase("Unknown")) {
                    missingDealFields++;
                }
            }

            String status = fields.getOrDefault("Deal Status", "");

            if (status.equalsIgnoreCase("Open"))
                openDeals++;

            else if (status.equalsIgnoreCase("On Hold"))
                onHoldDeals++;

            else if (status.equalsIgnoreCase("Closed"))
                closedDeals++;

            String probability = fields.getOrDefault("Closure Probability", "");

            if (probability.equalsIgnoreCase("High"))
                highProbability++;

            else if (probability.equalsIgnoreCase("Medium"))
                mediumProbability++;

            else if (probability.equalsIgnoreCase("Low"))
                lowProbability++;

            String amount = fields.getOrDefault("Masked Deal Value", "0");

            try {
                amount = amount.replace(",", "").trim();
                totalPipeline += Double.parseDouble(amount);
            } catch (Exception ignored) {
            }

            String owner = fields.getOrDefault("Owner code", "Unknown");

            ownerWiseDeals.put(owner,
                    ownerWiseDeals.getOrDefault(owner, 0) + 1);

            String sector = fields.getOrDefault("Sector/Service", "Unknown");

            sectorWiseDeals.put(sector,
                    sectorWiseDeals.getOrDefault(sector, 0) + 1);
        }

        int completedWO = 0;
        int ongoingWO = 0;
        int notStartedWO = 0;

        double totalReceivable = 0;

        Map<String, Integer> executionStatus = new HashMap<>();

        int missingWorkOrderFields = 0;

        for (WorkOrder workOrder : workOrders) {

            Map<String, String> fields = workOrder.getFields();

            for (String value : fields.values()) {

                if (value == null ||
                        value.isBlank() ||
                        value.equalsIgnoreCase("Unknown")) {

                    missingWorkOrderFields++;
                }
            }

            String status = fields.getOrDefault("Execution Status", "");

            if (status.equalsIgnoreCase("Completed"))
                completedWO++;

            else if (status.equalsIgnoreCase("Ongoing"))
                ongoingWO++;

            else if (status.equalsIgnoreCase("Not Started"))
                notStartedWO++;

            executionStatus.put(status,
                    executionStatus.getOrDefault(status, 0) + 1);

            String receivable =
                    fields.getOrDefault("Amount Receivable (Masked)", "0");

            try {
                receivable = receivable.replace(",", "").trim();
                totalReceivable += Double.parseDouble(receivable);
            } catch (Exception ignored) {
            }
        }

        summary.put("Open Deals", openDeals);
        summary.put("On Hold Deals", onHoldDeals);
        summary.put("Closed Deals", closedDeals);

        summary.put("High Probability Deals", highProbability);
        summary.put("Medium Probability Deals", mediumProbability);
        summary.put("Low Probability Deals", lowProbability);

        summary.put("Pipeline Value", totalPipeline);

        summary.put("Owner Wise Deals", ownerWiseDeals);
        summary.put("Sector Wise Deals", sectorWiseDeals);

        summary.put("Completed Work Orders", completedWO);
        summary.put("Ongoing Work Orders", ongoingWO);
        summary.put("Not Started Work Orders", notStartedWO);

        summary.put("Execution Status", executionStatus);

        summary.put("Total Receivable", totalReceivable);

        summary.put("Missing Deal Values", missingDealFields);
        summary.put("Missing Work Order Values", missingWorkOrderFields);

        return summary;
    }
}