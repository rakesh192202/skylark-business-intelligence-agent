package com.skylark.business_agent.controller;

import com.skylark.business_agent.dto.ChatRequest;
import com.skylark.business_agent.dto.ChatResponse;
import com.skylark.business_agent.service.BIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AIController {

    private final BIService biService;

    public AIController(BIService biService) {
        this.biService = biService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String answer = biService.processQuestion(request.getQuestion());

        return new ChatResponse(answer);
    }
}