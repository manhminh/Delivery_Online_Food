package com.backend.service;

import com.backend.model.Order;
import com.backend.response.PaymentResponse;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;

import java.io.IOException;

public interface PaymentService {
    PaymentResponse createPaymentLink(Order order) throws StripeException, IOException, WriterException;
}
