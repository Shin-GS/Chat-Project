package com.chat.server.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeApi {
    @GetMapping("/api/health-check")
    public String healthCheck() {
        return "health-check";
    }
}
