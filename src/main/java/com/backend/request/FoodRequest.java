package com.backend.request;

import com.backend.model.Category;
import com.backend.model.IngredientsItem;
import com.backend.model.Restaurant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FoodRequest {
    private String name;

    private String description;

    private Long price;

    private Category category;

    private List<String> images = new ArrayList<>();

    private boolean available;

    private Long restaurantId;

    private boolean isVegetarian;

    private boolean isSeasonal;

    private List<IngredientsItem> ingredients = new ArrayList<>();
}
