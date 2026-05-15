package com.smartshop.order.service;

import java.util.List;

import com.smartshop.order.dto.OrderRequest;
import com.smartshop.order.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByUserId(Long userId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(
        Long id, String status);
}