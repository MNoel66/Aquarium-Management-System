package com.ey.aquarium.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HomeController {
	
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Aquarium Management System API");
        response.put("status", "running");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "auth", "/api/auth/login, /api/auth/register",
            "tanks", "/api/tanks",
            "fishes", "/api/fishes",
            "tickets", "/api/tickets"
        ));
        return ResponseEntity.ok(response);
    }

}
