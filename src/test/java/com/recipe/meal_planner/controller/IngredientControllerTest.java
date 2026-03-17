package com.recipe.meal_planner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.meal_planner.advice.GlobalExceptionHandler;
import com.recipe.meal_planner.dto.IngredientDto;
import com.recipe.meal_planner.exception.IngredientNotFoundException;
import com.recipe.meal_planner.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IngredientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void create_whenValidIngredientsProvided_returnIngredient() throws Exception {
        IngredientDto request = createEggIngredientDto(0, "Kiaušinis");
        IngredientDto response = createEggIngredientDto(1L, "Kiaušinis");
        when(ingredientService.create(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(ingredientService).create(request);
    }

    @Test
    void create_whenIngredientNameIsMissing_returnBadRequest() throws Exception {
        IngredientDto request = createEggIngredientDto(0, null);

        mockMvc.perform(post("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(ingredientService, never()).create(any());
    }

    @Test
    void get_whenIngredientExists_returnIngredient() throws Exception {
        Long ingredientId = 1L;
        IngredientDto response = createEggIngredientDto(ingredientId, "Kiaušinis");
        when(ingredientService.get(ingredientId))
                .thenReturn(response);

        mockMvc.perform(get("/api/ingredients/{id}", ingredientId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(ingredientService).get(ingredientId);
    }

    @Test
    void get_whenIngredientNotFound_returnNotFound() throws Exception {
        Long ingredientId = 999L;
        when(ingredientService.get(ingredientId))
                .thenThrow(new IngredientNotFoundException(ingredientId));

        mockMvc.perform(get("/api/ingredients/{id}", ingredientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(ingredientId).getMessage()));

        verify(ingredientService).get(ingredientId);
    }

    @Test
    void getAll_whenIngredientsExist_returnIngredients() throws Exception {
        IngredientDto breadIngredientDto = new IngredientDto(2L, "Palangos šviesi duona", 244, 5.3,
                1.9, 44.3);

        List<IngredientDto> response = List.of(createEggIngredientDto(1L, "Kiaušinis"), breadIngredientDto);

        when(ingredientService.getAll())
                .thenReturn(response);

        mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(ingredientService).getAll();
    }

    @Test
    void update_whenIngredientExists_returnUpdatedIngredient() throws Exception {
        Long ingredientId = 1L;
        IngredientDto eggIngredientDto = createEggIngredientDto(ingredientId, "Kiaušinis");

        when(ingredientService.update(ingredientId, eggIngredientDto))
                .thenReturn(eggIngredientDto);

        mockMvc.perform(put("/api/ingredients/{id}", ingredientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eggIngredientDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(eggIngredientDto)));

        verify(ingredientService).update(ingredientId, eggIngredientDto);
    }

    @Test
    void update_whenIngredientNameIsMissing_returnBadRequest() throws Exception {
        Long ingredientId = 1L;
        IngredientDto ingredientDto = createEggIngredientDto(ingredientId, null);

        mockMvc.perform(put("/api/ingredients/{id}", ingredientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ingredientDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(ingredientService);
    }

    @Test
    void update_whenIngredientNotFound_returnNotFound() throws Exception {
        Long ingredientId = 999L;
        IngredientDto eggIngredientDto = createEggIngredientDto(ingredientId, "Kiaušinis");

        when(ingredientService.update(ingredientId, eggIngredientDto))
                .thenThrow(new IngredientNotFoundException((ingredientId)));

        mockMvc.perform(put("/api/ingredients/{id}", ingredientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eggIngredientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(ingredientId).getMessage()));

        verify(ingredientService).update(ingredientId, eggIngredientDto);
    }

    @Test
    void delete_whenIngredientExists_callsServiceDelete() throws Exception {
        Long ingredientId = 1L;

        mockMvc.perform(delete("/api/ingredients/{id}", ingredientId))
                .andExpect(status().isNoContent());

        verify(ingredientService).delete(ingredientId);
        verifyNoMoreInteractions(ingredientService);
    }

    @Test
    void delete_whenIngredientNotFound_returnNotFound() throws Exception {
        Long ingredientId = 999L;

        doThrow(new IngredientNotFoundException(ingredientId))
                .when(ingredientService).delete(ingredientId);

        mockMvc.perform(delete("/api/ingredients/{id}", ingredientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(new IngredientNotFoundException(ingredientId).getMessage()));

        verify(ingredientService).delete(ingredientId);
        verifyNoMoreInteractions(ingredientService);
    }

    private static IngredientDto createEggIngredientDto(long id, String name) {
        return new IngredientDto(id, name, 155, 13,
                11, 1.5);
    }
}
