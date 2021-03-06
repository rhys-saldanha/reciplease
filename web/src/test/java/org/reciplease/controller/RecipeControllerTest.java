package org.reciplease.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reciplease.dto.RecipeDto;
import org.reciplease.model.Ingredient;
import org.reciplease.model.Measure;
import org.reciplease.model.Recipe;
import org.reciplease.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RecipeService recipeService;

    @Test
    @DisplayName("ID does not exist")
    void noRecipe() throws Exception {
        final var uuid = UUID.randomUUID();
        when(recipeService.findById(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recipes/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("get recipe by ID")
    void recipe() throws Exception {
        final var soup = getSoup();
        final var soupDto = RecipeDto.from(soup);

        when(recipeService.findById(soup.getUuid())).thenReturn(Optional.of(soup));

        mockMvc.perform(get("/api/recipes/{uuid}", soup.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(soupDto), true));
    }

    private Recipe getSoup() {
        return Recipe.builder()
                .randomUUID()
                .name("soup")
                .build()
                .addIngredient(getTomato(), 5d);
    }

    private Ingredient getTomato() {
        return Ingredient.builder()
                .randomUUID()
                .name("tomato")
                .measure(Measure.ITEMS)
                .build();
    }

    @Test
    @DisplayName("get all recipes")
    void allRecipes() throws Exception {
        final var recipes = List.of(getToast(), getSoup());
        final var recipeDtoList = recipes.stream()
                .map(RecipeDto::from)
                .collect(toList());

        when(recipeService.findAll()).thenReturn(recipes);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(recipeDtoList), true));
    }

    private Recipe getToast() {
        return Recipe.builder()
                .randomUUID()
                .name("toast")
                .build()
                .addIngredient(getBread(), 1d);
    }

    private Ingredient getBread() {
        return Ingredient.builder()
                .randomUUID()
                .name("bread")
                .measure(Measure.ITEMS)
                .build();
    }
}