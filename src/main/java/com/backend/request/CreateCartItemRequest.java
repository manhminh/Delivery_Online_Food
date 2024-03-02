package com.backend.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCartItemRequest {
    private Long foodId;

    private int quantity;

    private List<String> ingredients = new ArrayList<>();

}
