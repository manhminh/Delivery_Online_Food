package com.backend.service;

import com.backend.exception.FoodExceptions;
import com.backend.model.Category;
import com.backend.model.Food;
import com.backend.model.Restaurant;
import com.backend.repository.FoodRepository;
import com.backend.request.FoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Food createFood(FoodRequest request, Category category, Restaurant restaurant) {
        System.out.println(request);
        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(request.getDescription());
        food.setImages(request.getImages());
        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setAvailable(true);
        food.setIngredients(request.getIngredients());
        food.setVegetarian(request.isVegetarian());
        food.setSeasonal(request.isSeasonal());
        food.setCreatedAt(new Date());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) {
        Food food = getFoodById(foodId);
        food.setRestaurant(null);
        foodRepository.delete(food);
    }

    @Override
    public List<Food> getRestaurantFoods(Long restaurantId, boolean isVegetarian, boolean isNonVeg, boolean isSeasonal, String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if(isVegetarian){
            foods = filterByVegetarian(foods, isVegetarian);
        }
        
        if(isNonVeg){
            foods = filterByNonVeg(foods, isNonVeg);
        }
        
        if(isSeasonal){
            foods = filterBySeasonal(foods, isSeasonal);
        }

        if(foodCategory != null && !foodCategory.isEmpty()){
            foods = filterByCategory(foods, foodCategory);
        }

        return foods;
    }

    public List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream()
                .filter(food -> food.getFoodCategory() != null
                        && food.getFoodCategory().getName().equals(foodCategory))
                .collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream()
                .filter(food -> food.isSeasonal() == isSeasonal)
                .collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean isNonVeg) {
        return foods.stream()
                .filter(food -> !food.isVegetarian())
                .collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream()
                .filter(food -> food.isVegetarian() == isVegetarian)
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFoods(String query) {
        return foodRepository.searchFoods(query);
    }

    @Override
    public Food getFoodById(Long foodId) {
        Optional<Food> food = foodRepository.findById(foodId);
        if(food.isEmpty()) {
            throw new FoodExceptions("Food not found");
        }
        return food.get();
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) {
        Food food = getFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
