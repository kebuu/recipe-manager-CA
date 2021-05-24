package fr.cta.recipe.management.ui.config;

import com.vaadin.flow.spring.annotation.UIScope;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.entity.RecipeOwner;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.usecase.*;
import fr.cta.recipe.management.domain.utils.RandomDomainUtils;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import org.apache.commons.collections4.SetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Configuration
public class SpringConfiguration {

    private final Set<Recipe> recipes = Set.of(
        RandomDomainUtils.randomRecipe(),
        RandomDomainUtils.randomRecipe(),
        new Recipe(
            UUID.fromString("d98beaad-fc9f-481a-b85e-283a413b6926"),
            RandomDomainUtils.randomString(),
            List.of(RandomDomainUtils.randomQuantifiedIngredient()),
            RandomDomainUtils.randomListOfString()
        )
    );
    private final AtomicReference<RecipeOwner> recipeOwnerReference = new AtomicReference<>(RandomDomainUtils.randomRecipeOwner());

    @Bean
    public RecipeRepository recipeRepository() {
        AtomicReference<Set<Recipe>> recipesReference = new AtomicReference<>(recipes);

        return new RecipeRepository() {

            @Override
            public Optional<Recipe> getById(UUID recipeId) {
                return recipesReference.get().stream()
                    .filter(recipe -> recipe.getId().equals(recipeId))
                    .findFirst();
            }

            @Override
            public void update(Recipe recipe) {
                Set<Recipe> recipeInSet = Set.of(recipe);
                recipesReference.updateAndGet(recipes1 -> SetUtils.union(SetUtils.difference(recipes1, recipeInSet), recipeInSet));
            }

            @Override
            public void deleteById(UUID recipeId) {
                recipesReference.updateAndGet(recipes1 -> recipes1.stream().filter(recipe -> !recipe.getId().equals(recipeId)).collect(Collectors.toSet()));
            }

            @Override
            public Set<Recipe> findByOwnerId(UUID recipeOwnerId) {
                return recipesReference.get();
            }

            @Override
            public void addRecipeToOwner(Recipe recipe, UUID id) {
                recipesReference.updateAndGet(recipes -> SetUtils.union(recipes, Collections.singleton(recipe)));
            }
        };
    }

    @Bean
    public GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase(RecipeRepository recipeRepository) {
        return new GetRecipesByOwnerIdUseCase(recipeRepository);
    }

    @Bean
    public RecipeOwnerRepository recipeOwnerRepository(RecipeRepository recipeRepository) {
        return new RecipeOwnerRepository() {

            @Override
            public Optional<RecipeOwner> getOwnerById(UUID ownerId) {
                return Optional.of(recipeOwnerReference.get());
            }

            @Override
            public Optional<RecipeOwner> getOwnerByRecipeId(UUID recipeId) {
                recipeOwnerReference.updateAndGet(recipeOwner -> {
                    RecipeOwner recipeOwner1 = recipeOwner.toBuilder()
                        .recipeInfos(List.of())
                        .build();

                    for (Recipe recipe : recipeRepository.findByOwnerId(null)) {
                        recipeOwner1 = recipeOwner1.updateRecipe(recipe);
                    }
                    return recipeOwner1;
                });

                return Optional.of(recipeOwnerReference.get());
            }
        };
    }

    @Bean
    public UpdateRecipeUseCase updateRecipeUseCase(RecipeRepository recipeRepository, RecipeOwnerRepository recipeOwnerRepository) {
        return new UpdateRecipeUseCase(recipeRepository, recipeOwnerRepository);
    }

    @Bean
    public AddRecipeUseCase addRecipeUseCase(RecipeRepository recipeRepository, RecipeOwnerRepository recipeOwnerRepository) {
        return new AddRecipeUseCase(recipeRepository, recipeOwnerRepository);
    }

    @Bean
    public DeleteRecipeUseCase deleteRecipeUseCase(RecipeRepository recipeRepository) {
        return new DeleteRecipeUseCase(recipeRepository);
    }

    @Bean
    public GetRecipeByIdUseCase getRecipeByIdUseCase(RecipeRepository recipeRepository) {
        return new GetRecipeByIdUseCase(recipeRepository);
    }

    @Bean
    @UIScope
    public AppActionDispatcher appActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase, UpdateRecipeUseCase updateRecipeUseCase, AddRecipeUseCase addRecipeUseCase, DeleteRecipeUseCase deleteRecipeUseCase, GetRecipeByIdUseCase getRecipeByIdUseCase) {
        return new AppActionDispatcher(getRecipesByOwnerIdUseCase, updateRecipeUseCase, addRecipeUseCase, deleteRecipeUseCase, getRecipeByIdUseCase);
    }
}
