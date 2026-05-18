package com.smartshop.payment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.payment.entity.Payment;
import com.smartshop.payment.repostory.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>>
            getPaymentsByOrderId(
                @PathVariable Long orderId) {
        return ResponseEntity.ok(
            paymentRepository
                .findByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>>
            getPaymentsByUserId(
                @PathVariable Long userId) {
        return ResponseEntity.ok(
            paymentRepository
                .findByUserId(userId));
    }

    @GetMapping
    public ResponseEntity<List<Payment>>
            getAllPayments() {
        return ResponseEntity.ok(
            paymentRepository.findAll());
    }
}