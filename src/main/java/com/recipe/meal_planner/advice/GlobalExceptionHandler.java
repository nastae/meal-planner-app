package com.recipe.meal_planner.advice;

import com.recipe.meal_planner.exception.DuplicateIngredientException;
import com.recipe.meal_planner.exception.IngredientNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IngredientNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleIngredientNotFoundException(IngredientNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(DuplicateIngredientException.class)
    @ResponseStatus(CONFLICT)
    @ResponseBody
    public Map<String, String> handleDuplicateIngredient(DuplicateIngredientException ex) {
        return Map.of("message", ex.getMessage());
    }
}
