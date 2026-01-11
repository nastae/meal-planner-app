package com.recipe.meal_planner.dto;

public record IngredientDto(long id, String name, double kcalPer100, double proteinPer100, double fatPer100, double carbsPer100) {
}
