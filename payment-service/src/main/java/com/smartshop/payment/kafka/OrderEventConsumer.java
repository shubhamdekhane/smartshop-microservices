package com.smartshop.payment.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.smartshop.payment.entity.Payment;
import com.smartshop.payment.repostory.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentRepository paymentRepository;

    @KafkaListener(
        topics = "order-events",
        groupId = "payment-group")
    public void consumeOrderEvent(OrderEvent event) {
        System.out.println(
            "Received order event: " + event);

        // Process payment automatically
        Payment payment = Payment.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .amount(event.getTotalPrice())
                .status("SUCCESS")
                .paymentMethod("ONLINE")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        System.out.println(
            "Payment processed for order: "
            + event.getOrderId());
    }
}