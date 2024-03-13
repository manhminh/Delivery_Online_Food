package com.backend.service;

import com.backend.model.Order;
import com.backend.response.PaymentResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImp implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse createPaymentLink(Order order) throws StripeException, IOException, WriterException {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(
                        SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success/" + order.getId())
                .setCancelUrl("http://localhost:3000/payment/failed")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) order.getTotalPrice() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Food")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentUrl(session.getUrl());

        // Generate QR code
        paymentResponse.setQrCode(generateQrCode(session.getUrl()));

        return paymentResponse;
    }

    private byte[] generateQrCode(String text) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
