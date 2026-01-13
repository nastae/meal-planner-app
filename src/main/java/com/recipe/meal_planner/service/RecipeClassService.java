package com.recipe.meal_planner.service;

import com.recipe.meal_planner.dto.RecipeClassDto;
import com.recipe.meal_planner.model.RecipeClass;
import com.recipe.meal_planner.repository.RecipeClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeClassService {

    private final RecipeClassRepository repository;

    public RecipeClassDto create(RecipeClassDto dto) {
        RecipeClass rc = new RecipeClass();
        rc.setTitle(dto.title());
        rc.setInstructions(dto.instructions());
        rc = repository.save(rc);
//        dto.setId(rc.getId());
//        return dto;
        return toDto(rc);
    }

    public RecipeClassDto get(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("RecipeClass not found"));
    }

    //TODO: update to show List<Recipe>
    public List<RecipeClassDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RecipeClassDto update(Long id, RecipeClassDto dto) {
        RecipeClass rc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RecipeClass not found"));
        rc.setTitle(dto.title());
        rc.setInstructions(dto.instructions());
        repository.save(rc);
        return toDto(rc);
    }

//    TODO: maybe use soft delete
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private RecipeClassDto toDto(RecipeClass rc) {
        return new RecipeClassDto(rc.getId(), rc.getTitle(), rc.getInstructions());
    }
}
