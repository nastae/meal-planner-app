package com.recipe.meal_planner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.exception.IngredientNotFoundException;
import com.recipe.meal_planner.model.Ingredient;
import com.recipe.meal_planner.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class IngredientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void cleanDatabase() {
        ingredientRepository.deleteAll();
    }

    @Test
    void create_whenValidIngredientProvided_returnCreatedIngredient() throws Exception {
        IngredientDto eggIngredientDto = createEggIngredientDto(0L);
        IngredientDto createdEggIngredientDto = createEggIngredientDto(1L);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eggIngredientDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdEggIngredientDto)));

        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertEquals(1, ingredients.size());

        Ingredient expectedIngredient = createIngredient(createdEggIngredientDto);
        Ingredient savedIngredient = ingredients.getFirst();

        assertAll("Saved ingredient fields",
                () -> assertNotNull(savedIngredient.getId()),
                () -> assertEquals(savedIngredient.getName(), savedIngredient.getName()),
                () -> assertEquals(expectedIngredient.getKcalPer100(), savedIngredient.getKcalPer100()),
                () -> assertEquals(expectedIngredient.getProteinPer100(), savedIngredient.getProteinPer100()),
                () -> assertEquals(expectedIngredient.getFatPer100(), savedIngredient.getFatPer100()),
                () -> assertEquals(expectedIngredient.getCarbsPer100(), savedIngredient.getCarbsPer100())
        );
    }

    @Test
    void get_whenIngredientExists_returnIngredient() throws Exception {
        Ingredient eggIngredient = ingredientRepository.save(createEggIngredient());
        IngredientDto expectedIngredientDto = createIngredientDto(eggIngredient);

        mockMvc.perform(get("/api/ingredients/{id}", eggIngredient.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedIngredientDto)));
    }

    @Test
    void get_whenIngredientNotFound_returnNotFound() throws Exception {
        Long missingId = 999L;

        mockMvc.perform(get("/api/ingredients/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(missingId).getMessage()));
    }

    private static IngredientDto createIngredientDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getName(), ingredient.getKcalPer100(),
                ingredient.getProteinPer100(), ingredient.getFatPer100(), ingredient.getCarbsPer100());
    }

    private static Ingredient createEggIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Kiaušinis");
        ingredient.setKcalPer100(155.0);
        ingredient.setProteinPer100(13.0);
        ingredient.setFatPer100(11.0);
        ingredient.setCarbsPer100(1.5);
        return ingredient;
    }

    private static Ingredient createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.name());
        ingredient.setKcalPer100(ingredientDto.kcalPer100());
        ingredient.setProteinPer100(ingredientDto.proteinPer100());
        ingredient.setFatPer100(ingredientDto.fatPer100());
        ingredient.setCarbsPer100(ingredientDto.carbsPer100());
        return ingredient;
    }

    private static IngredientDto createEggIngredientDto(long id) {
        return new IngredientDto(id, "Kiaušinis", 155, 13,
                11, 1.5);
    }
}
