package com.recipe.meal_planner.controller;

import com.recipe.meal_planner.dto.RecipeDto;
import com.recipe.meal_planner.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService service;

    @PostMapping
    public RecipeDto create(@RequestBody RecipeDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public RecipeDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> update(@PathVariable Long id, @RequestBody RecipeDto recipeDto) {
        RecipeDto updated = service.update(id, recipeDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
