package com.smartshop.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.smartshop.notification.kafka.OrderEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(
            OrderEvent event) {
        SimpleMailMessage message =
            new SimpleMailMessage();
        message.setTo("dekhaneshubham@gmail.com");
        message.setSubject(
            "Order Confirmation #"
            + event.getOrderId());
        message.setText(
            "Dear Customer,\n\n" +
            "Your order has been placed!\n\n" +
            "Order ID: " + event.getOrderId() + "\n" +
            "Total Amount: ₹" + event.getTotalPrice() + "\n" +
            "Status: " + event.getStatus() + "\n\n" +
            "Thank you for shopping with SmartShop!"
        );
        mailSender.send(message);
        System.out.println(
            "Email sent for order: "
            + event.getOrderId());
    }
}