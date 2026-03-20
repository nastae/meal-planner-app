package com.recipe.meal_planner.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.exception.DuplicateIngredientException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                .contentType(APPLICATION_JSON)
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
    void create_whenIngredientNameAlreadyExistsWithIgnoreCase_returnConflict() throws Exception {
        ingredientRepository.save(createEggIngredient("kIAUŠINIS"));
        IngredientDto eggIngredientDto = createEggIngredientDto(0L, "Kiaušinis");

        mockMvc.perform(post("/api/ingredients")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eggIngredientDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(
                        new DuplicateIngredientException(eggIngredientDto.name()).getMessage()));
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
        Long missingIngredientId = 999L;

        mockMvc.perform(get("/api/ingredients/{id}", missingIngredientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(missingIngredientId).getMessage()));
    }

    @Test
    void getAll_whenIngredientsExist_returnIngredients() throws Exception {
        Ingredient eggIngredient = ingredientRepository.save(createEggIngredient());
        Ingredient breadIngredient = ingredientRepository.save(createBreadIngredient());
        List<IngredientDto> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(createIngredientDto(eggIngredient));
        expectedIngredients.add(createIngredientDto(breadIngredient));
        expectedIngredients.sort(Comparator.comparing(IngredientDto::id));

        mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedIngredients)))
                .andExpect(jsonPath("$.length()").value(2));

        assertEquals(2, ingredientRepository.count());
    }

    @Test
    void update_whenIngredientExists_returnUpdatedIngredient() throws Exception {
        Ingredient eggIngredient = ingredientRepository.save(createEggIngredient());

        IngredientDto updatedEggIngredientDto = createChickenEggIngredientDto(0L);
        IngredientDto expectedEggIngredientDto = createChickenEggIngredientDto(eggIngredient.getId());

        mockMvc.perform(put("/api/ingredients/{id}", eggIngredient.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEggIngredientDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedEggIngredientDto)));

        Ingredient updatedIngredient = ingredientRepository.findById(eggIngredient.getId()).orElseThrow();
        assertAll("Updated ingredient fields",
                () -> assertEquals(expectedEggIngredientDto.id(), updatedIngredient.getId()),
                () -> assertEquals(expectedEggIngredientDto.name(), updatedIngredient.getName()),
                () -> assertEquals(expectedEggIngredientDto.kcalPer100(), updatedIngredient.getKcalPer100()),
                () -> assertEquals(expectedEggIngredientDto.proteinPer100(), updatedIngredient.getProteinPer100()),
                () -> assertEquals(expectedEggIngredientDto.fatPer100(), updatedIngredient.getFatPer100()),
                () -> assertEquals(expectedEggIngredientDto.carbsPer100(), updatedIngredient.getCarbsPer100())
        );
    }

    @Test
    void update_whenIngredientNotFound_returnNotFound() throws Exception {
        Long missingIngredientId = 999L;
        IngredientDto updatedEggIngredientDto = createEggIngredientDto(0L);

        mockMvc.perform(put("/api/ingredients/{id}", missingIngredientId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEggIngredientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(missingIngredientId).getMessage()));

        assertEquals(0, ingredientRepository.count());
    }

    @Test
    void delete_whenIngredientExists_removesIngredient() throws Exception {
        Ingredient eggIngredient = ingredientRepository.save(createEggIngredient());

        mockMvc.perform(delete("/api/ingredients/{id}", eggIngredient.getId()))
                .andExpect(status().isNoContent());

        assertTrue(ingredientRepository.findById(eggIngredient.getId()).isEmpty());
    }

    @Test
    void delete_whenIngredientNotFound_returnNotFound() throws Exception {
        Long missingIngredientId = 999L;

        mockMvc.perform(delete("/api/ingredients/{id}", missingIngredientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(missingIngredientId).getMessage()));
    }

    private static IngredientDto createChickenEggIngredientDto(Long id) {
        return new IngredientDto(id, "Vištos kiaušinis",
                160, 12, 10, 2);
    }

    private static IngredientDto createIngredientDto(Ingredient ingredient) {
        return new IngredientDto(ingredient.getId(), ingredient.getName(), ingredient.getKcalPer100(),
                ingredient.getProteinPer100(), ingredient.getFatPer100(), ingredient.getCarbsPer100());
    }

    private static Ingredient createEggIngredient() {
        return createEggIngredient("Kiaušinis");
    }

    private static Ingredient createEggIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setKcalPer100(155.0);
        ingredient.setProteinPer100(13.0);
        ingredient.setFatPer100(11.0);
        ingredient.setCarbsPer100(1.5);
        return ingredient;
    }

    private static Ingredient createBreadIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Palangos šviesi duona");
        ingredient.setKcalPer100(224.0);
        ingredient.setProteinPer100(5.3);
        ingredient.setFatPer100(1.9);
        ingredient.setCarbsPer100(44.3);
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
        return createEggIngredientDto(id, "Kiaušinis");
    }

    private static IngredientDto createEggIngredientDto(long id, String name) {
        return new IngredientDto(id, name, 155, 13,
                11, 1.5);
    }
}
