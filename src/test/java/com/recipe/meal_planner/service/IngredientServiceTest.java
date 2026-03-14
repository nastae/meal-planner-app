package com.recipe.meal_planner.service;

import com.recipe.meal_planner.dto.IngredientDto;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void get_whenIngredientsExist_returnIngredient() {
        Ingredient ingredient = createEggIngredient();

        when(ingredientRepository.findById(anyLong()))
                .thenReturn(Optional.of(ingredient));

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
    void get_whenIngredientsNotFound_throwRuntimeException() {
        when(ingredientRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ingredientService.get(1L));

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
        assertEquals(224.0, bread.kcalPer100(), 0.001);
        assertEquals(5.3, bread.proteinPer100());
        assertEquals(1.9, bread.fatPer100());
        assertEquals(44.3, bread.carbsPer100());
        verify(ingredientRepository).findAll();
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
}
