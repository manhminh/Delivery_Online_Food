package com.backend.controller;

import com.backend.model.Food;
import com.backend.model.User;
import com.backend.service.FoodService;
import com.backend.service.RestaurantService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFoods(
            @RequestParam String query,
            @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foods = foodService.searchFoods(query);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFoods(
            @PathVariable Long restaurantId,
            @RequestParam boolean isVegetarian,
            @RequestParam boolean isNonVeg,
            @RequestParam boolean isSeasonal,
            @RequestParam(required = false) String foodCategory,
            @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Food> foods = foodService.getRestaurantFoods(restaurantId, isVegetarian, isNonVeg, isSeasonal, foodCategory);

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
