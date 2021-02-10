package fr.cta.recipe.management.domain.repository;

import fr.cta.recipe.management.domain.entity.Recipe;

import java.util.*;

public interface RecipeRepository {

    Optional<Recipe> getById(UUID recipeId);

    void update(Recipe recipe);

    void deleteById(UUID recipeId);

    Set<Recipe> findByOwnerId(UUID recipeOwnerId);

    void addRecipeToOwner(Recipe recipe, UUID id);
}
