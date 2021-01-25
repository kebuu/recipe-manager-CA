package fr.cta.recipe.management.domain.repository;

import fr.cta.recipe.management.domain.entity.Recipe;

import java.util.UUID;

public interface RecipeRepository {

    Recipe getById(UUID recipeId);

    void insert(Recipe recipe);

    void update(Recipe recipe);

    void deleteById(UUID recipeId);
}
