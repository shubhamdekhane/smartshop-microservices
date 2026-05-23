package com.smartshop.gateway.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@GetMapping("/order")
    public ResponseEntity<Map<String, Object>> orderFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "status", "CIRCUIT_OPEN",
                "service", "Order Service",
                "message", "Order service is currently unavailable. Please try again later.",
                "timestamp", LocalDateTime.now().toString()
            ));
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> userFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "status", "CIRCUIT_OPEN",
                "service", "User Service",
                "message", "User service is currently unavailable.",
                "timestamp", LocalDateTime.now().toString()
            ));
    }

    @GetMapping("/product")
    public ResponseEntity<Map<String, Object>> productFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of(
                "status", "CIRCUIT_OPEN",
                "service", "Product Service",
                "message", "Product service is currently unavailable.",
                "timestamp", LocalDateTime.now().toString()
            ));
    }
}