package com.backend.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentUrl;

    private byte[] qrCode;
}
