package com.recipe.meal_planner.dto;

import com.recipe.meal_planner.model.MealType;

import java.util.List;

public record RecipeDto(long id, long recipeClassId, MealType mealType, List<RecipeIngredientDto> recipeIngredients, List<Long> subRecipeIds) {
}
