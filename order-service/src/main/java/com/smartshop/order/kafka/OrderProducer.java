package com.smartshop.order.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent>
        kafkaTemplate;

    public static final String ORDER_TOPIC =
        "order-events";

    public void sendOrderEvent(OrderEvent event) {
        kafkaTemplate.send(ORDER_TOPIC, event);
        System.out.println(
            "Order event sent to Kafka: " + event);
    }
}
