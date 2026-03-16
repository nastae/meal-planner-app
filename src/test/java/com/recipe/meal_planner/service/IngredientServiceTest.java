package com.recipe.meal_planner.service;

import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.exception.IngredientNotFoundException;
import com.recipe.meal_planner.model.Ingredient;
import com.recipe.meal_planner.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void get_whenIngredientsExist_returnIngredient() {
        when(ingredientRepository.findById(anyLong()))
                .thenReturn(Optional.of(createEggIngredient()));

        IngredientDto ingredientDto = ingredientService.get(1L);

        assertNotNull(ingredientDto);
        assertEquals("Kiaušinis", ingredientDto.name());
        assertEquals(155.0, ingredientDto.kcalPer100());
        assertEquals(13.0, ingredientDto.proteinPer100());
        assertEquals(11.0, ingredientDto.fatPer100());
        assertEquals(1.5, ingredientDto.carbsPer100());
        verify(ingredientRepository).findById(1L);
    }

    @Test
    void get_whenIngredientsNotFound_throwException() {
        when(ingredientRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IngredientNotFoundException.class,
                () -> ingredientService.get(1L));
        assertEquals("Ingredient not found with id 1", exception.getMessage());

        verify(ingredientRepository).findById(1L);
    }

    @Test
    void create_whenValidIngredientsProvided_returnIngredient() {
        IngredientDto ingredientDto = new IngredientDto(0L, "Kiaušinis", 155.0, 13.0,
                11.0, 1.5);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName(ingredientDto.name());
        ingredient.setKcalPer100(ingredientDto.kcalPer100());
        ingredient.setProteinPer100(ingredientDto.proteinPer100());
        ingredient.setFatPer100(ingredientDto.fatPer100());
        ingredient.setCarbsPer100(ingredientDto.carbsPer100());

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        when(ingredientRepository.save(any()))
                .thenReturn(ingredient);

        IngredientDto result = ingredientService.create(ingredientDto);
        assertNotNull(result);
        assertEquals("Kiaušinis", result.name());
        assertEquals(155.0, result.kcalPer100(), 0.001);
        assertEquals(13.0, result.proteinPer100());
        assertEquals(11.0, result.fatPer100());
        assertEquals(1.5, result.carbsPer100());
        verify(ingredientRepository).save(captor.capture());
        Ingredient savedIngredient = captor.getValue();
        assertNotNull(savedIngredient);
        assertEquals("Kiaušinis", savedIngredient.getName());
        assertEquals(155.0, savedIngredient.getKcalPer100(), 0.001);
        assertEquals(13.0, savedIngredient.getProteinPer100());
        assertEquals(11.0, savedIngredient.getFatPer100());
        assertEquals(1.5, savedIngredient.getCarbsPer100());
    }

    @Test
    void getAll_whenIngredientsExist_returnIngredients() {
        Ingredient breadIngredient = new Ingredient();
        breadIngredient.setId(2L);
        breadIngredient.setName("Palangos šviesi duona");
        breadIngredient.setKcalPer100(224.0);
        breadIngredient.setProteinPer100(5.3);
        breadIngredient.setFatPer100(1.9);
        breadIngredient.setCarbsPer100(44.3);

        when(ingredientRepository.findAll())
                .thenReturn(List.of(createEggIngredient(), breadIngredient));

        List<IngredientDto> result = ingredientService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        IngredientDto egg = result.get(0);
        assertEquals("Kiaušinis", egg.name());
        assertEquals(155.0, egg.kcalPer100(), 0.001);
        assertEquals(13.0, egg.proteinPer100());
        assertEquals(11.0, egg.fatPer100());
        assertEquals(1.5, egg.carbsPer100());
        IngredientDto bread = result.get(1);
        assertEquals("Palangos šviesi duona", bread.name());
        assertEquals(224.0, bread.kcalPer100());
        assertEquals(5.3, bread.proteinPer100());
        assertEquals(1.9, bread.fatPer100());
        assertEquals(44.3, bread.carbsPer100());
        verify(ingredientRepository).findAll();
    }

    @Test
    void update_whenIngredientExists_returnUpdatedIngredient() {
        IngredientDto ingredientDto = createChickenEggIngredientDto();

        Ingredient egg = createEggIngredient();

        Ingredient updatedEgg = new Ingredient();
        updatedEgg.setId(egg.getId());
        updatedEgg.setName(ingredientDto.name());
        updatedEgg.setKcalPer100(ingredientDto.kcalPer100());
        updatedEgg.setProteinPer100(ingredientDto.proteinPer100());
        updatedEgg.setFatPer100(ingredientDto.fatPer100());
        updatedEgg.setCarbsPer100(ingredientDto.carbsPer100());

        when(ingredientRepository.findById(1L))
                .thenReturn(Optional.of(egg));

        when(ingredientRepository.save(any()))
                .thenReturn(updatedEgg);

        IngredientDto result = ingredientService.update(1L, ingredientDto);
        assertNotNull(result);
        assertEquals(ingredientDto.name(), result.name());
        assertEquals(ingredientDto.kcalPer100(), result.kcalPer100());
        assertEquals(ingredientDto.proteinPer100(), result.proteinPer100());
        assertEquals(ingredientDto.fatPer100(), result.fatPer100());
        assertEquals(ingredientDto.carbsPer100(), result.carbsPer100());

        verify(ingredientRepository).findById(1L);

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(captor.capture());
        Ingredient savedIngredient = captor.getValue();
        assertNotNull(savedIngredient);
        assertEquals(ingredientDto.name(), savedIngredient.getName());
        assertEquals(egg.getId(), savedIngredient.getId());
        assertEquals(ingredientDto.kcalPer100(), savedIngredient.getKcalPer100());
        assertEquals(ingredientDto.proteinPer100(), savedIngredient.getProteinPer100());
        assertEquals(ingredientDto.fatPer100(), savedIngredient.getFatPer100());
        assertEquals(ingredientDto.carbsPer100(), savedIngredient.getCarbsPer100());
    }

    @Test
    void update_whenIngredientNotFound_throwsException() {
        Long ingredientId = 1L;
        when(ingredientRepository.findById(ingredientId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IngredientNotFoundException.class,
                () -> ingredientService.update(ingredientId, createChickenEggIngredientDto()));
        assertEquals("Ingredient not found with id 1", exception.getMessage());

        verify(ingredientRepository).findById(ingredientId);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void delete_whenIngredientExists_callRepositoryDeleteBy() {
        Long ingredientId = 1L;
        Ingredient eggIngredient = createEggIngredient();

        when(ingredientRepository.findById(ingredientId))
                .thenReturn(Optional.of(eggIngredient));

        ingredientService.delete(ingredientId);

        verify(ingredientRepository).findById(ingredientId);
        verify(ingredientRepository).delete(eggIngredient);
        verifyNoMoreInteractions(ingredientRepository);
    }

    @Test
    void delete_whenIngredientNotFound_throwsException() {
        Long ingredientId = 1L;

        when(ingredientRepository.findById(ingredientId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IngredientNotFoundException.class,
                () -> ingredientService.delete(ingredientId));
        assertEquals("Ingredient not found with id 1", exception.getMessage());

        verify(ingredientRepository).findById(ingredientId);
        verifyNoMoreInteractions(ingredientRepository);
    }

    private static Ingredient createEggIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Kiaušinis");
        ingredient.setKcalPer100(155.0);
        ingredient.setProteinPer100(13.0);
        ingredient.setFatPer100(11.0);
        ingredient.setCarbsPer100(1.5);
        return ingredient;
    }

    private static IngredientDto createChickenEggIngredientDto() {
        return new IngredientDto(0, "Paukščio kiaušinis", 160.0, 12.0,
                10.0, 2.0);
    }
}
