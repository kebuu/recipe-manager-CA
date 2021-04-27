package fr.cta.recipe.management.ui.config;

import com.vaadin.flow.spring.annotation.UIScope;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.entity.RecipeOwner;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.domain.usecase.UpdateRecipeUseCase;
import fr.cta.recipe.management.domain.utils.RandomDomainUtils;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class SpringConfiguration {

    @Bean
    public RecipeRepository recipeRepository() {
        Set<Recipe> recipes = Set.of(
            RandomDomainUtils.randomRecipe(),
            RandomDomainUtils.randomRecipe(),
            new Recipe(
                UUID.fromString("d98beaad-fc9f-481a-b85e-283a413b6926"),
                RandomDomainUtils.randomString(),
                List.of(RandomDomainUtils.randomQuantifiedIngredient()),
                RandomDomainUtils.randomListOfString()
            )
        );

        return new RecipeRepository() {

            @Override
            public Optional<Recipe> getById(UUID recipeId) {
                return Optional.empty();
            }

            @Override
            public void update(Recipe recipe) {

            }

            @Override
            public void deleteById(UUID recipeId) {

            }

            @Override
            public Set<Recipe> findByOwnerId(UUID recipeOwnerId) {
                return recipes;
            }

            @Override
            public void addRecipeToOwner(Recipe recipe, UUID id) {

            }
        };
    }

    @Bean
    public GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase(RecipeRepository recipeRepository) {
        return new GetRecipesByOwnerIdUseCase(recipeRepository);
    }

    @Bean
    public RecipeOwnerRepository recipeOwnerRepository() {
        RecipeOwner recipeOwner = RandomDomainUtils.randomRecipeOwner();
        return new RecipeOwnerRepository() {

            @Override
            public Optional<RecipeOwner> getOwnerById(UUID ownerId) {
                return Optional.empty();
            }

            @Override
            public Optional<RecipeOwner> getOwnerByRecipeId(UUID ownerId) {
                return Optional.of(recipeOwner);
            }
        };
    }

    @Bean
    public UpdateRecipeUseCase updateRecipeUseCase(RecipeRepository recipeRepository, RecipeOwnerRepository recipeOwnerRepository) {
        return new UpdateRecipeUseCase(recipeRepository, recipeOwnerRepository);
    }

    @Bean
    @UIScope
    public AppActionDispatcher appActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase, UpdateRecipeUseCase updateRecipeUseCase) {
        return new AppActionDispatcher(getRecipesByOwnerIdUseCase, updateRecipeUseCase);
    }
}
