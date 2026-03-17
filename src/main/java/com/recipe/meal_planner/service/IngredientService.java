package com.recipe.meal_planner.service;

import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.exception.IngredientNotFoundException;
import com.recipe.meal_planner.model.Ingredient;
import com.recipe.meal_planner.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientDto create(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.name());
        ingredient.setKcalPer100(dto.kcalPer100());
        ingredient.setProteinPer100(dto.proteinPer100());
        ingredient.setFatPer100(dto.fatPer100());
        ingredient.setCarbsPer100(dto.carbsPer100());
        ingredient = ingredientRepository.save(ingredient);
//        dto.id(ingredient.getId().longValue());
//        return dto;
        return toDto(ingredient);
    }

    public IngredientDto get(Long id) {
        return ingredientRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public List<IngredientDto> getAll() {
        return ingredientRepository.findAll(Sort.by("id"))
                .stream()
                .map(this::toDto)
//                .sorted(Comparator.comparing(IngredientDto::id))
                .collect(Collectors.toList());
    }

    public IngredientDto update(Long id, IngredientDto dto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
        ingredient.setName(dto.name());
        ingredient.setKcalPer100(dto.kcalPer100());
        ingredient.setProteinPer100(dto.proteinPer100());
        ingredient.setFatPer100(dto.fatPer100());
        ingredient.setCarbsPer100(dto.carbsPer100());
        ingredientRepository.save(ingredient);
        return toDto(ingredient);
    }

    public void delete(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                        .orElseThrow(() -> new IngredientNotFoundException(id));
        ingredientRepository.delete(ingredient);
    }

    private IngredientDto toDto(Ingredient ingredient) {
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getKcalPer100(),
                ingredient.getProteinPer100(),
                ingredient.getFatPer100(),
                ingredient.getCarbsPer100());
    }
}
