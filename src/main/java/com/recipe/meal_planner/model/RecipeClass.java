package com.recipe.meal_planner.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_class")
//RecipeClass is just instructions, ingredients, etc.
//MealType only matters when you assign that recipe to a specific meal slot.
public class RecipeClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

//    @Column(length = 2000)
    @Column(length = 5000)
    private String instructions;

    // optional: link to recipe instances
//    @OneToMany(mappedBy = "recipeClass")
    @OneToMany(mappedBy = "recipeClass", cascade = CascadeType.ALL)
//    private List<Recipe> recipes;
    private List<Recipe> recipes = new ArrayList<>();
}
