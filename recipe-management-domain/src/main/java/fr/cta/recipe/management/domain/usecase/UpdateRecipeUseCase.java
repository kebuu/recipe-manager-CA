package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.entity.RecipeOwner;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;

public class UpdateRecipeUseCase implements UseCase<Recipe, Void> {

    static final String CANNOT_FIND_OWNER_OF_RECIPE_ERROR_MSG_TEMPLATE = "Cannot find owner of recipe with id %s";

    private final RecipeRepository recipeRepository;
    private final RecipeOwnerRepository recipeOwnerRepository;

    public UpdateRecipeUseCase(RecipeRepository recipeRepository, RecipeOwnerRepository recipeOwnerRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeOwnerRepository = recipeOwnerRepository;
    }

    @Override
    public Void execute(Recipe recipe) {
        RecipeOwner recipeOwner = recipeOwnerRepository.getOwnerByRecipeId(recipe.getId())
            .orElseThrow(() -> new IllegalStateException(CANNOT_FIND_OWNER_OF_RECIPE_ERROR_MSG_TEMPLATE.formatted(recipe.getId())));

        recipeOwner.updateRecipe(recipe);

        recipeRepository.update(recipe);
        return null;
    }
}
