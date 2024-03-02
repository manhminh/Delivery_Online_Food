package com.backend.service;


import com.backend.model.IngredientCategory;
import com.backend.model.IngredientsItem;

import java.util.List;

public interface IngredientService {
    IngredientCategory createIngredientCategory(String name, Long restaurantId);

    IngredientCategory findIngredientCategoryById(Long id);

    List<IngredientCategory> findIngredientCategoriesByRestaurantId(Long restaurantId);

    IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId);

    List<IngredientsItem> findRestaurantIngredients(Long restaurantId);

    IngredientsItem updateStock(Long id);
}
