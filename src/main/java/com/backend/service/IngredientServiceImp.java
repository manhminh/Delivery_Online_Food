package com.backend.service;

import com.backend.exception.IngredientException;
import com.backend.model.IngredientCategory;
import com.backend.model.IngredientsItem;
import com.backend.model.Restaurant;
import com.backend.repository.IngredientCategoryRepository;
import com.backend.repository.IngredientsItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImp implements IngredientService {

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private IngredientsItemRepository ingredientsItemRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        IngredientCategory category = new IngredientCategory();
        category.setName(name);
        category.setRestaurant(restaurant);

        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) {
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(id);
        if(ingredientCategory.isEmpty()) {
            throw new IngredientException("Ingredient category not found");
        }

        return ingredientCategory.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoriesByRestaurantId(Long restaurantId) {
        restaurantService.getRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(categoryId);

        IngredientsItem item = new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        IngredientsItem savedItem = ingredientsItemRepository.save(item);
        category.getIngredients().add(savedItem);

        return savedItem;
    }

    @Override
    public List<IngredientsItem> findRestaurantIngredients(Long restaurantId) {
        restaurantService.getRestaurantById(restaurantId);
        return ingredientsItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) {
        Optional<IngredientsItem> ingredientsItem = ingredientsItemRepository.findById(id);
        if(ingredientsItem.isEmpty()) {
            throw new IngredientException("Ingredient not found");
        }

        IngredientsItem item = ingredientsItem.get();
        item.setInStock(!item.isInStock());

        return ingredientsItemRepository.save(item);
    }
}
