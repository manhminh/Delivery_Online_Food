package com.backend.service;

import com.backend.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long userId);

    List<Category> findCategoriesByRestaurantId(Long restaurantId);

    Category findCategoryById(Long id);
}
