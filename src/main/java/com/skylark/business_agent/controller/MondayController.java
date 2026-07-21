package com.skylark.business_agent.controller;

import com.skylark.business_agent.model.Deal;
import com.skylark.business_agent.model.WorkOrder;
import com.skylark.business_agent.service.MondayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MondayController {

    private final MondayService mondayService;

    public MondayController(MondayService mondayService) {
        this.mondayService = mondayService;
    }

    // Fetch all boards
    @GetMapping("/boards")
    public String getBoards() {
        return mondayService.getBoards();
    }

    // Raw Deals JSON
    @GetMapping("/deals")
    public String getDeals() {
        return mondayService.getDeals();
    }

    // Raw Work Orders JSON
    @GetMapping("/workorders")
    public String getWorkOrders() {
        return mondayService.getWorkOrders();
    }

    // Parsed Deals (Java Objects)
    @GetMapping("/deals-java")
    public List<Deal> getDealsJava() throws Exception {
        return mondayService.getDealsData();
    }

    // Parsed Work Orders (Java Objects)
    @GetMapping("/workorders-java")
    public List<WorkOrder> getWorkOrdersJava() throws Exception {
        return mondayService.getWorkOrdersData();
    }

    // Debug Deals
    @GetMapping("/debug/deals")
    public String debugDeals() {
        return mondayService.getDeals();
    }

    // Debug Work Orders
    @GetMapping("/debug/workorders")
    public String debugWorkOrders() {
        return mondayService.getWorkOrders();
    }

    // Debug Boards (NEW)
    @GetMapping("/debug/boards")
    public String debugBoards() {
        return mondayService.getBoards();
    }
}