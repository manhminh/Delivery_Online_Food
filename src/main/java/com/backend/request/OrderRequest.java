package com.backend.request;

import com.backend.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;

    private Address deliveryAddress;
}
