package com.backend.controller;

import com.backend.model.Restaurant;
import com.backend.model.User;
import com.backend.request.RestaurantRequest;
import com.backend.response.MessageResponse;
import com.backend.service.RestaurantService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequest request, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.createRestaurant(request, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody RestaurantRequest request,
                                                       @RequestHeader("Authorization") String jwt,
                                                       @PathVariable Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurant(restaurantId, request);

        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@RequestHeader("Authorization") String jwt,
                                                            @PathVariable Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        restaurantService.deleteRestaurant(restaurantId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Restaurant deleted successfully");

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{restaurantId}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(@RequestHeader("Authorization") String jwt,
                                                             @PathVariable Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.updateRestaurantStatus(restaurantId);

        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> getRestaurantByUserId(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
