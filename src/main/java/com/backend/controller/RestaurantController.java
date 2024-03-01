package com.backend.controller;

import com.backend.dto.RestaurantDTO;
import com.backend.model.Restaurant;
import com.backend.model.User;
import com.backend.service.RestaurantService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestHeader("Authorization") String jwt, @RequestParam String query) {
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.searchRestaurants(query);

        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Restaurant>> getAllRestaurants(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@RequestHeader("Authorization") String jwt, @PathVariable Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/add-to-favorites")
    public ResponseEntity<RestaurantDTO> addToFavorites(@RequestHeader("Authorization") String jwt, @PathVariable Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        RestaurantDTO restaurantDTO = restaurantService.addToFavorites(restaurantId, user);

        return new ResponseEntity<>(restaurantDTO, HttpStatus.OK);
    }
}
