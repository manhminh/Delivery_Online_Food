package com.backend.controller;

import com.backend.model.Order;
import com.backend.model.User;
import com.backend.request.OrderRequest;
import com.backend.response.PaymentResponse;
import com.backend.service.OrderService;
import com.backend.service.PaymentService;
import com.backend.service.UserService;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/orders")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest request,
                                                       @RequestHeader("Authorization") String jwt) throws StripeException, IOException, WriterException {
        User user = userService.findUserByJwtToken(jwt);
        Order createdOrder = orderService.createOrder(request, user);
        PaymentResponse paymentResponse = paymentService.createPaymentLink(createdOrder);

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/orders/user")
    public ResponseEntity<List<Order>> getUserOrderHistory(
            @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUserOrders(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
