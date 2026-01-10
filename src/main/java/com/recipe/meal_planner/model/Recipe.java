package com.recipe.meal_planner.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String title;

//    @Column(length = 2000)
//    private String instructions;

    @ManyToOne
    private RecipeClass recipeClass;

//    1. Kcal and quantity can differ per meal
//    Breakfast omelette might be 300 kcal, dinner version 600 kcal.
//    If you put MealType in RecipeClass, you’d have to create multiple RecipeClasses for the same instructions just to assign different meals → duplicate canonical recipes unnecessarily.
//
//   2. Flexibility for weekly plans / rules
//   The same canonical recipe can appear as breakfast one day and lunch another day with different portion sizes.
    @Enumerated(EnumType.STRING)
    private MealType mealType; // BREAKFAST, LUNCH, DINNER, SNACK

//    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    // list of ingredients and quantities for this specific recipe
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    // Optional: composite recipes
    // Composite recipes (this recipe is made of other recipes)
    @ManyToMany
    @JoinTable(
            name = "composite_recipe",
            joinColumns = @JoinColumn(name = "parent_recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "child_recipe_id")
    )
    private List<Recipe> subRecipes = new ArrayList<>();

    public double getTotalKcal() {
//        TODO: rewrite to use MeasurementUnit PIECE
//        return recipeIngredients.stream()
//                .mapToDouble(ri -> ri.getIngredient().getKcalPer100() * ri.getQuantity() / 100)
//                .sum();
        double subRecipesTotalKcal = subRecipes.stream()
                .mapToDouble(Recipe::getTotalKcal)
                .sum();
        return subRecipesTotalKcal + recipeIngredients.stream()
                .mapToDouble(ri -> {
                    double quantityInBase = ri.getQuantityInGrams();//ri.getQuantity() * ri.getQuantityInGrams();//ri.getUnit().getToBaseFactor();
                    return ri.getIngredient().getKcalPer100() * quantityInBase / 100;
                })
                .sum();
    }

    public double getTotalProtein() {
        //        TODO: rewrite to use MeasurementUnit PIECE
//        return recipeIngredients.stream()
//                .mapToDouble(ri -> ri.getIngredient().getProteinPer100() * ri.getQuantity() / 100)
//                .sum();
        double subRecipesTotalProtein = subRecipes.stream()
                .mapToDouble(Recipe::getTotalProtein)
                .sum();
        return subRecipesTotalProtein + recipeIngredients.stream()
                .mapToDouble(ri -> {
                    double quantityInBase = ri.getQuantityInGrams();//ri.getQuantity() * ri.getQuantityInGrams();//ri.getUnit().getToBaseFactor();
                    return ri.getIngredient().getProteinPer100() * quantityInBase / 100;
                })
                .sum();
    }

//

//    private String name;
//    TODO: update to enum
//    Type safety.
//    With String, you can accidentally write "breakfastt" or "Lunch" (case mismatch).
//    Enum ensures only allowed values exist: BREAKFAST, LUNCH, DINNER, SNACK.
//    Future proof
//    If you later add or remove meal types, you change only the enum, not scattered string values in code.
//    Using enums is still “future-proof” for coding and type safety, but you need a tiny bit of care when removing enum values from production data.
//    How to handle it safely:
//    Option 1: Soft delete → Don’t delete enum values, mark them as deprecated in code.
//    Option 2: Data migration → Update DB rows before removing the enum value.
//    Option 3: Keep enum values for historical data → Even if you stop using them in logic, don’t delete from enum immediately.
//    @Enumerated(EnumType.STRING)
//    private MealType mealType; // enum instead of String
//    private String mealType; // breakfast, lunch, dinner, snack
//    TOOD: receptas gali tureti produktus
//    TODO: receptas gali tureti laiko paruosimo laika
//    private String newField;
}
