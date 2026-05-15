package com.smartshop.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartshop.order.dto.OrderRequest;
import com.smartshop.order.dto.OrderResponse;
import com.smartshop.order.dto.ProductResponse;
import com.smartshop.order.entity.Order;
import com.smartshop.order.feign.ProductClient;
import com.smartshop.order.kafka.OrderEvent;
import com.smartshop.order.kafka.OrderProducer;
import com.smartshop.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderProducer orderProducer;

    @Override
    public OrderResponse createOrder(
            OrderRequest request) {

        // Call Product Service via Feign
        ProductResponse product = productClient
            .getProductById(request.getProductId());

        // Calculate total price
        Double totalPrice = product.getPrice()
            * request.getQuantity();

        // Create order
        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Publish to Kafka
        OrderEvent event = OrderEvent.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus())
                .build();

        orderProducer.sendOrderEvent(event);

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Order not found: " + id));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(
            Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(
            Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Order not found: " + id));
        order.setStatus(status);
        return mapToResponse(
            orderRepository.save(order));
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}