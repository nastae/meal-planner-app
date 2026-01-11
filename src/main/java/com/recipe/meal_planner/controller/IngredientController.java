package com.recipe.meal_planner.controller;


import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.service.IngredientService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService service;

    @PostMapping
    public IngredientDto create(@RequestBody IngredientDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public IngredientDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<IngredientDto> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public IngredientDto update(@PathVariable Long id, @RequestBody IngredientDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
