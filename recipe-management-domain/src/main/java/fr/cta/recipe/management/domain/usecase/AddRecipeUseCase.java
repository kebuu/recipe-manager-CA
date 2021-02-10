package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.entity.RecipeOwner;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import lombok.Value;

import java.util.UUID;

public class AddRecipeUseCase implements UseCase<AddRecipeUseCase.Data, Void> {

    static final String MISSING_OWNER_ERROR_MSG_TEMPLATE = "Missing owner with id : %s";

    private final RecipeRepository recipeRepository;
    private final RecipeOwnerRepository recipeOwnerRepository;

    public AddRecipeUseCase(RecipeRepository recipeRepository, RecipeOwnerRepository recipeOwnerRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeOwnerRepository = recipeOwnerRepository;
    }

    @Override
    public Void execute(AddRecipeUseCase.Data data) {
        RecipeOwner recipeOwner = recipeOwnerRepository.getOwnerById(data.getOwnerId())
            .orElseThrow(() -> new IllegalStateException(MISSING_OWNER_ERROR_MSG_TEMPLATE.formatted(data.getOwnerId())));

        RecipeOwner updatedRecipeOwner = recipeOwner.addRecipe(data.getRecipe());

        recipeRepository.addRecipeToOwner(data.getRecipe(), updatedRecipeOwner.getId());

        return null;
    }

    @Value
    public static class Data {
        UUID ownerId;
        Recipe recipe;
    }
}
