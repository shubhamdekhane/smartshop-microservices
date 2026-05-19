package com.smartshop.payment.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;


import com.smartshop.payment.kafka.OrderEvent;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, OrderEvent>
            consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:9092");
        config.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            "payment-group");
        config.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            "earliest");
        config.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        config.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);

        // THIS IS THE KEY FIX!
        // Tell consumer exactly which class to use
        config.put(
            JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(
            JsonDeserializer.VALUE_DEFAULT_TYPE,
            "com.smartshop.payment.kafka.OrderEvent");
        config.put(
            JsonDeserializer.USE_TYPE_INFO_HEADERS,
            false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory
            <String, OrderEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory
            <String, OrderEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}