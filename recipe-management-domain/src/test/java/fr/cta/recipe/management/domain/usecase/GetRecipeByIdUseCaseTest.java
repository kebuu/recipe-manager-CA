package fr.cta.recipe.management.domain.usecase;


import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class GetRecipeByIdUseCaseTest {

    @InjectMocks private GetRecipeByIdUseCase getRecipeUseCase;
    @Mock private RecipeRepository recipeRepository;

    @Test
    void shouldGetRecipeById() {
        UUID recipeId = UUID.randomUUID();
        Recipe recipe = TestUtils.randomRecipe();
        Optional<Recipe> maybeRecipe = Optional.of(recipe);
        Mockito.when(recipeRepository.getById(recipeId)).thenReturn(maybeRecipe);

        Assertions.assertThat(getRecipeUseCase.execute(recipeId))
            .contains(recipe);
    }
}