package com.recipe.meal_planner.exception;

public class DuplicateIngredientException extends RuntimeException {

    public DuplicateIngredientException(String name) {
        super("Ingredient with name " + name + " already exists");
    }
}
