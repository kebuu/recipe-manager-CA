package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeRepository;

import java.util.Optional;
import java.util.UUID;

public class GetRecipeByIdUseCase implements UseCase<UUID, Optional<Recipe>> {

    private final RecipeRepository recipeRepository;

    public GetRecipeByIdUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<Recipe> execute(UUID recipeId) {
        return recipeRepository.getById(recipeId);
    }
}
