package com.backend.service;

import com.backend.model.Category;
import com.backend.model.Food;
import com.backend.model.Restaurant;
import com.backend.request.FoodRequest;

import java.util.List;

public interface FoodService {
    Food createFood(FoodRequest request, Category category, Restaurant restaurant);

    void deleteFood(Long foodId);

    List<Food> getRestaurantFoods(Long restaurantId,
                                      boolean isVegetarian,
                                      boolean isNonVeg,
                                      boolean isSeasonal,
                                      String foodCategory
    );

    List<Food> searchFoods(String query);

    Food getFoodById(Long foodId);

    Food updateAvailabilityStatus(Long foodId);
}
