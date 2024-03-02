package com.backend.service;

import com.backend.model.Order;
import com.backend.model.User;
import com.backend.request.OrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest request, User user);

    Order updateOrderStatus(Long orderId, String status);

    void cancelOrder(Long orderId);

    Order findOrderById(Long orderId);

    List<Order> getUserOrders(Long userId);

    List<Order> getRestaurantOrders(Long restaurantId, String status);
}
