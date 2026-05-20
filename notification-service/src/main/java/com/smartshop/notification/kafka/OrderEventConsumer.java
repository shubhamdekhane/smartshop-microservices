package com.smartshop.notification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.smartshop.notification.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
        topics = "order-events",
        groupId = "notification-group")
    public void consumeOrderEvent(
            OrderEvent event) {
        System.out.println(
            "Notification Service received: "
            + event);

        // Send email notification
        emailService.sendOrderConfirmation(event);
    }
}