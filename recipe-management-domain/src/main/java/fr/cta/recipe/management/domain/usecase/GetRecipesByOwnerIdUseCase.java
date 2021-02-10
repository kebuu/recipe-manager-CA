package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeRepository;

import java.util.*;

public class GetRecipesByOwnerIdUseCase implements UseCase<UUID, Set<Recipe>> {

    private final RecipeRepository recipeRepository;

    public GetRecipesByOwnerIdUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> execute(UUID recipeOwnerId) {
        return recipeRepository.findByOwnerId(recipeOwnerId);
    }
}
