package com.backend.repository;

import com.backend.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByRestaurantId(Long restaurantId);

    @Query("SELECT f FROM Food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
            "OR LOWER(f.foodCategory.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Food> searchFoods(@Param("query") String query);
}
