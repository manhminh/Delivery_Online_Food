package com.backend.controller;

import com.backend.model.Category;
import com.backend.model.User;
import com.backend.service.CategoryService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Category createdCategory = categoryService.createCategory(category.getName(), user.getId());

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getRestaurantCategories(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("restaurantId") Long restaurantId) {
        User user = userService.findUserByJwtToken(jwt);
        List<Category> categories = categoryService.findCategoriesByRestaurantId(restaurantId);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
