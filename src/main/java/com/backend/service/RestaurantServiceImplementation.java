package com.backend.service;

import com.backend.dto.RestaurantDTO;
import com.backend.exception.RestaurantException;
import com.backend.model.Address;
import com.backend.model.Restaurant;
import com.backend.model.User;
import com.backend.repository.AddressRepository;
import com.backend.repository.RestaurantRepository;
import com.backend.repository.UserRepository;
import com.backend.request.RestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImplementation implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(RestaurantRequest request, User user) {
        Address address = addressRepository.save(request.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(request.getContactInformation());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setDescription(request.getDescription());
        restaurant.setName(request.getName());
        restaurant.setImages(request.getImages());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, RestaurantRequest request) {
        Restaurant restaurant = getRestaurantById(restaurantId);

        if(request.getCuisineType() != null) {
            restaurant.setCuisineType(request.getCuisineType());
        }

        if(restaurant.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }

        if(restaurant.getName() != null) {
            restaurant.setName(request.getName());
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurants(String query) {
        return restaurantRepository.searchRestaurants(query);
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        if(restaurant.isEmpty()) {
           throw new RestaurantException("Restaurant not found");
        }

        return restaurant.get();
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);

        if (restaurant == null) {
            throw new RestaurantException("Restaurant not found");
        }

        return restaurant;
    }

    @Override
    public RestaurantDTO addToFavorites(Long restaurantId, User user) {
        Restaurant restaurant = getRestaurantById(restaurantId);

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurantId);
        restaurantDTO.setDescription(restaurant.getDescription());
        restaurantDTO.setImages(restaurant.getImages());
        restaurantDTO.setTitle(restaurant.getName());

        boolean isFavorited = false;
        List<RestaurantDTO> favorites = user.getFavorites();
        for(RestaurantDTO favorite : favorites) {
            if(favorite.getId().equals(restaurantId)) {
                isFavorited = true;
                break;
            }
        }

        if(isFavorited) {
           favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(restaurantDTO);
        }

        userRepository.save(user);
        return restaurantDTO;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.setOpen(!restaurant.isOpen());

        return restaurantRepository.save(restaurant);
    }
}
