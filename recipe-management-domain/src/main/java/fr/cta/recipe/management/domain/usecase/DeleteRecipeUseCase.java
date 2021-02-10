package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.repository.RecipeRepository;

import java.util.UUID;

public class DeleteRecipeUseCase implements UseCase<UUID, Void> {

    private final RecipeRepository recipeRepository;

    public DeleteRecipeUseCase(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Void execute(UUID recipeId) {
        recipeRepository.deleteById(recipeId);
        return null;
    }
}
