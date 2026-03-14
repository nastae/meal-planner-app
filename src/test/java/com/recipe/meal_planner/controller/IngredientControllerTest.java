package com.recipe.meal_planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void create_whenValidIngredientsProvided_returnIngredient() throws Exception {
        IngredientDto request = new IngredientDto(0, "Kiaušinis", 155, 13,
                11, 1.5);
        IngredientDto response = new IngredientDto(1L, "Kiaušinis", 155, 13,
                11, 1.5);
        when(ingredientService.create(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void create_whenIngredientNameIsMissing_returnBadRequest() throws Exception {
        IngredientDto request = new IngredientDto(0, null, 155, 13, 11, 1.5);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
