package com.recipe.meal_planner.dto;

import com.recipe.meal_planner.model.MeasurementUnit;

public record RecipeIngredientDto(long ingredientId, MeasurementUnit unit, double quantity, double pieceWeight) {
}
