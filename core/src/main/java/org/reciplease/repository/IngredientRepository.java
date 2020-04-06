package org.reciplease.repository;

import org.reciplease.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {
    List<Ingredient> findByNameContains(String searchName);
}
