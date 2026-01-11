package com.recipe.meal_planner.repository;

import com.recipe.meal_planner.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Optional: add custom query methods if needed
}
