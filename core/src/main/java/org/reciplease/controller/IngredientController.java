package org.reciplease.controller;

import lombok.RequiredArgsConstructor;
import org.reciplease.model.Ingredient;
import org.reciplease.repository.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    final IngredientRepository ingredientRepository;

    @GetMapping("{id}")
    public ResponseEntity<Ingredient> findById(@PathVariable final String id) {
        final Optional<Ingredient> foundIngredient = ingredientRepository.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(foundIngredient.get());
    }

    @PostMapping
    public ResponseEntity<Ingredient> create(@Valid @RequestBody final Ingredient ingredient) {
        final Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
    }
}
