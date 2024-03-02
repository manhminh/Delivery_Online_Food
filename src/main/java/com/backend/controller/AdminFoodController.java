package com.backend.controller;

import com.backend.model.Food;
import com.backend.model.Restaurant;
import com.backend.model.User;
import com.backend.request.FoodRequest;
import com.backend.response.MessageResponse;
import com.backend.service.FoodService;
import com.backend.service.RestaurantService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<Food> createFood(@RequestBody FoodRequest request,
                                           @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());
        Food food = foodService.createFood(request, request.getCategory(), restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<MessageResponse> deleteFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long foodId) {
        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(foodId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Food deleted successfully");

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
   }

   @PutMapping("/{foodId}/status")
   public ResponseEntity<Food> updateAvailabilityStatusFood(
           @RequestHeader("Authorization") String jwt,
           @PathVariable Long foodId) {
       User user = userService.findUserByJwtToken(jwt);
       Food food = foodService.updateAvailabilityStatus(foodId);

       return new ResponseEntity<>(food, HttpStatus.OK);
   }
}
