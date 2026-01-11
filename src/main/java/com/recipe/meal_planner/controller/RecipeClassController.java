package com.recipe.meal_planner.controller;

import com.recipe.meal_planner.dto.RecipeClassDto;
import com.recipe.meal_planner.service.RecipeClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-classes")
@RequiredArgsConstructor
public class RecipeClassController {

    private final RecipeClassService service;

    @PostMapping
    public RecipeClassDto create(@RequestBody RecipeClassDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public RecipeClassDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<RecipeClassDto> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public RecipeClassDto update(@PathVariable Long id, @RequestBody RecipeClassDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
