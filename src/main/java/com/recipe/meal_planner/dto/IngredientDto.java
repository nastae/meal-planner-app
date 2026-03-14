package com.recipe.meal_planner.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record IngredientDto(long id, @NotNull(message = "Name is required") String name,
                            @Positive(message = "Kcal must be positive") double kcalPer100,
                            @Positive(message = "Protein must be positive") double proteinPer100,
                            @Positive(message = "Fat must be positive") double fatPer100,
                            @Positive(message = "Carbs must be positive") double carbsPer100) {
}
