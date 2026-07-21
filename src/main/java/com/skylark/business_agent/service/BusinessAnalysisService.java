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

        int missingDealFields = 0;

        for (Deal deal : deals) {

            for (String value : deal.getFields().values()) {

                if (value == null ||
                        value.isBlank() ||
                        value.equalsIgnoreCase("Unknown")) {

                    missingDealFields++;
                }
            }
        }

        int missingWorkOrderFields = 0;

        for (WorkOrder workOrder : workOrders) {

            for (String value : workOrder.getFields().values()) {

                if (value == null ||
                        value.isBlank() ||
                        value.equalsIgnoreCase("Unknown")) {

                    missingWorkOrderFields++;
                }
            }
        }

        summary.put("Missing Deal Values", missingDealFields);
        summary.put("Missing Work Order Values", missingWorkOrderFields);

        return summary;
    }
}