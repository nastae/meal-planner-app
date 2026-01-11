package com.recipe.meal_planner.repository;

import com.recipe.meal_planner.model.RecipeClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeClassRepository extends JpaRepository<RecipeClass, Long> {
}
