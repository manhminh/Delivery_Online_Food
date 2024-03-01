package com.backend.service;

import com.backend.dto.RestaurantDTO;
import com.backend.model.Restaurant;
import com.backend.model.User;
import com.backend.request.RestaurantRequest;

import java.util.List;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantRequest request, User user);

    Restaurant updateRestaurant(Long restaurantId, RestaurantRequest request);

    void deleteRestaurant(Long restaurantId);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> searchRestaurants(String query);

    Restaurant getRestaurantById(Long restaurantId);

    Restaurant getRestaurantByUserId(Long userId);

    RestaurantDTO addToFavorites(Long restaurantId, User user);

    Restaurant updateRestaurantStatus(Long restaurantId);
}
