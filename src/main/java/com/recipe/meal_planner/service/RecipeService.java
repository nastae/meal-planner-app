package com.recipe.meal_planner.service;

import com.recipe.meal_planner.dto.RecipeDto;
import com.recipe.meal_planner.dto.RecipeIngredientDto;
//import com.recipe.meal_planner.entity.*;
import com.recipe.meal_planner.model.Ingredient;
import com.recipe.meal_planner.model.Recipe;
import com.recipe.meal_planner.model.RecipeClass;
import com.recipe.meal_planner.model.RecipeIngredient;
import com.recipe.meal_planner.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeClassRepository recipeClassRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeDto create(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setMealType(dto.mealType());

        RecipeClass rc = recipeClassRepository.findById(dto.recipeClassId())
                .orElseThrow(() -> new RuntimeException("RecipeClass not found"));
        recipe.setRecipeClass(rc);

        // RecipeIngredients
        List<RecipeIngredient> riList = new ArrayList<>();
        for (RecipeIngredientDto ridto : dto.recipeIngredients()) {
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredientRepository.findById(ridto.ingredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found")));
            ri.setUnit(ridto.unit());
            ri.setQuantity(ridto.quantity());
            ri.setPieceWeight(ridto.pieceWeight());
            riList.add(ri);
        }
        recipe.setRecipeIngredients(riList);

        // Sub-recipes
        List<Recipe> subRecipes = new ArrayList<>();
        if (dto.subRecipeIds() != null) {
            for (Long subId : dto.subRecipeIds()) {
                Recipe sub = recipeRepository.findById(subId)
                        .orElseThrow(() -> new RuntimeException("Sub-recipe not found"));
                subRecipes.add(sub);
            }
        }
        recipe.setSubRecipes(subRecipes);

        recipe = recipeRepository.save(recipe);
//        dto.setId(recipe.getId());
//        return dto;
        return toDto(recipe);
    }

    public RecipeDto get(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return toDto(recipe);
    }

    public List<RecipeDto> getAll() {
        return recipeRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RecipeDto update(Long id, RecipeDto dto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        // Update recipeClass if changed
        if (!recipe.getRecipeClass().getId().equals(dto.recipeClassId())) {
            RecipeClass rc = recipeClassRepository.findById(dto.recipeClassId())
                    .orElseThrow(() -> new RuntimeException("RecipeClass not found"));
            recipe.setRecipeClass(rc);
        }

        // Update mealType
        recipe.setMealType(dto.mealType());

        // Update ingredients
        // Clear existing ingredients and set new ones
        recipe.getRecipeIngredients().clear();
        if (dto.recipeIngredients() != null) {
            for (RecipeIngredientDto ridto : dto.recipeIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ridto.ingredientId())
                        .orElseThrow(() -> new RuntimeException("Ingredient not found"));
                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipe(recipe);
                ri.setIngredient(ingredient);
                ri.setQuantity(ridto.quantity());
                ri.setUnit(ridto.unit());
                ri.setPieceWeight(ridto.pieceWeight());
                recipe.getRecipeIngredients().add(ri);
            }
        }

        // Update sub-recipes
        recipe.getSubRecipes().clear();
        if (dto.subRecipeIds() != null) {
            for (Long subId : dto.subRecipeIds()) {
                Recipe subRecipe = recipeRepository.findById(subId)
                        .orElseThrow(() -> new RuntimeException("Sub-recipe not found: " + subId));
                recipe.getSubRecipes().add(subRecipe);
            }
        }

        recipe = recipeRepository.save(recipe);

        // Convert ingredients to DTO
        // Convert back to DTO including ID
        List<RecipeIngredientDto> ingredientDtos = recipe.getRecipeIngredients().stream()
                .map(ri -> new RecipeIngredientDto(
                        ri.getIngredient().getId(),
                        ri.getUnit(),
                        ri.getQuantity(),
                        ri.getPieceWeight()
                ))
                .toList();

        // Convert sub-recipe IDs
        List<Long> subIds = recipe.getSubRecipes().stream()
                .map(Recipe::getId)
                .toList();

        return new RecipeDto(
                recipe.getId(),
                recipe.getRecipeClass().getId(),
                recipe.getMealType(),
                ingredientDtos,
                subIds
        );
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    private RecipeDto toDto(Recipe recipe) {
        List<RecipeIngredientDto> recipeIngredientDtos = recipe.getRecipeIngredients().stream().map(ri -> {
            return new RecipeIngredientDto(
                    ri.getIngredient().getId(),
                    ri.getUnit(),
                    ri.getQuantity(),
                    ri.getPieceWeight()
            );
//            ridto.setIngredientId(ri.getIngredient().getId());
//            ridto.setUnit(ri.getUnit());
//            ridto.setQuantity(ri.getQuantity());
//            ridto.setPieceWeight(ri.getPieceWeight());
//            return ridto;
        }).collect(Collectors.toList());
        return new RecipeDto(
                recipe.getId(),
                recipe.getRecipeClass().getId(),
                recipe.getMealType(),
                recipeIngredientDtos,
                recipe.getSubRecipes().stream().map(Recipe::getId).collect(Collectors.toList())
                );
//        dto.setId(recipe.getId());
//        dto.setMealType(recipe.getMealType());
//        dto.setRecipeClassId(recipe.getRecipeClass().getId());
//        dto.setRecipeIngredients(recipe.getRecipeIngredients().stream().map(ri -> {
//            RecipeIngredientDto ridto = new RecipeIngredientDto();
//            ridto.setIngredientId(ri.getIngredient().getId());
//            ridto.setUnit(ri.getUnit());
//            ridto.setQuantity(ri.getQuantity());
//            ridto.setPieceWeight(ri.getPieceWeight());
//            return ridto;
//        }).collect(Collectors.toList()));
//        dto.setSubRecipeIds(recipe.getSubRecipes().stream().map(Recipe::getId).collect(Collectors.toList()));
//        return dto;
    }
}
