package com.backend.service;

import com.backend.exception.OrderException;
import com.backend.model.*;
import com.backend.repository.*;
import com.backend.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Order createOrder(OrderRequest request, User user) {
        Address shipAddress= request.getDeliveryAddress();
        Address savedAddress = addressRepository.save(shipAddress);

        if(!user.getAddresses().contains(savedAddress)) {
           user.getAddresses().add(savedAddress);
           userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());

        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setCustomer(user);
        order.setCreatedAt(new Date());
        order.setDeliveryAddress(savedAddress);
        order.setOrderStatus("PENDING");

        Cart cart = cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.getIngredients().addAll(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getPrice());

            OrderItem savedItem = orderItemRepository.save(orderItem);
            orderItems.add(savedItem);
        }

        Long totalPrice = cartService.calculateCartTotals(cart);

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        restaurant.getOrders().add(savedOrder);

        return savedOrder;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = findOrderById(orderId);
        if(status.equals("OUT_FOR_DELIVERY")
                || status.equals("DELIVERED")
                || status.equals("COMPLETED")
                || status.equals("PENDING")
        ) {
            order.setOrderStatus(status);
            return orderRepository.save(order);
        }
        throw new OrderException("Please select an valid order status");
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        orderRepository.delete(order);
    }

    @Override
    public Order findOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()) {
            throw new OrderException("Order not found");
        }

        return order.get();
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String status) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if(status != null) {
            return orders.stream()
                    .filter(order -> order.getOrderStatus().equals(status))
                    .collect(Collectors.toList());
        }

        return orders;
    }
}
