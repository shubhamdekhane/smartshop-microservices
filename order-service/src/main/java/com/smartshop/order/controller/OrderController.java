package com.smartshop.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.order.dto.OrderRequest;
import com.smartshop.order.dto.OrderResponse;
import com.smartshop.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>>
            getOrdersByUserId(
                @PathVariable Long userId) {
        return ResponseEntity.ok(
            orderService.getOrdersByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>>
            getAllOrders() {
        return ResponseEntity.ok(
            orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse>
            updateOrderStatus(
                @PathVariable Long id,
                @RequestParam String status) {
        return ResponseEntity.ok(
            orderService.updateOrderStatus(
                id, status));
    }
}