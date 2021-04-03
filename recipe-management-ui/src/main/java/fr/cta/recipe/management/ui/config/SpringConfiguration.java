package fr.cta.recipe.management.ui.config;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.domain.utils.RandomDomainUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class SpringConfiguration {

    @Bean
    public RecipeRepository recipeRepository() {
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
                return Set.of(
                    RandomDomainUtils.randomRecipe(),
                    RandomDomainUtils.randomRecipe(),
                    new Recipe(
                        UUID.fromString("d98beaad-fc9f-481a-b85e-283a413b6926"),
                        RandomDomainUtils.randomString(),
                        List.of(RandomDomainUtils.randomQuantifiedIngredient()),
                        RandomDomainUtils.randomListOfString()
                    )
                );
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
}
