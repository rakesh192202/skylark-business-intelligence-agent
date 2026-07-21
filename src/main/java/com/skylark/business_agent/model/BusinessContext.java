package com.skylark.business_agent.model;

import java.util.List;

public class BusinessContext {

    private List<Deal> deals;
    private List<WorkOrder> workOrders;

    public BusinessContext(List<Deal> deals, List<WorkOrder> workOrders) {
        this.deals = deals;
        this.workOrders = workOrders;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }
}