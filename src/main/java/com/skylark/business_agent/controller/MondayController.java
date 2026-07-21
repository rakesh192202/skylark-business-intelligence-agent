package com.skylark.business_agent.controller;

import com.skylark.business_agent.service.MondayService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MondayController {

    private final MondayService mondayService;

    public MondayController(MondayService mondayService) {
        this.mondayService = mondayService;
    }

    @GetMapping("/boards")
    public String getBoards() {
        return mondayService.getBoards();
    }
    @GetMapping("/deals")
public String getDeals() {
    return mondayService.getDeals();
}
@GetMapping("/workorders")
public String workOrders() {
    return mondayService.getWorkOrders();
}
}