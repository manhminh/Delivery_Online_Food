package com.backend.controller;

import com.backend.model.IngredientCategory;
import com.backend.model.IngredientsItem;
import com.backend.request.IngredientCategoryRequest;
import com.backend.request.IngredientsItemRequest;
import com.backend.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest request) {
        IngredientCategory category = ingredientService.createIngredientCategory(request.getName(), request.getRestaurantId());

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<IngredientsItem> createIngredientItem(
            @RequestBody IngredientsItemRequest request) {
        IngredientsItem item = ingredientService.createIngredientItem(request.getRestaurantId(), request.getName(), request.getCategoryId());

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<IngredientsItem> updateStock(
            @PathVariable("id") Long id) {
        IngredientsItem item = ingredientService.updateStock(id);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredients(
            @PathVariable("restaurantId") Long restaurantId) {
        List<IngredientsItem> ingredients = ingredientService.findRestaurantIngredients(restaurantId);

        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(
            @PathVariable("restaurantId") Long restaurantId) {
        List<IngredientCategory> ingredients = ingredientService.findIngredientCategoriesByRestaurantId(restaurantId);

        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }
}