package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateRecipeUseCaseTest {


    @InjectMocks private UpdateRecipeUseCase updateRecipeUseCase;
    @Mock private RecipeRepository recipeRepository;
    @Mock private RecipeOwnerRepository recipeOwnerRepository;

    @Test
    void shouldFailUpdateRecipeUseCase_missingOwner() {
        Recipe recipe = TestUtils.randomRecipe();
        Mockito.when(recipeOwnerRepository.getOwnerByRecipeId(recipe.getId())).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = Assertions.catchThrowableOfType(() -> updateRecipeUseCase.execute(recipe), IllegalStateException.class);
        Assertions.assertThat(illegalStateException).hasMessage(UpdateRecipeUseCase.CANNOT_FIND_OWNER_OF_RECIPE_ERROR_MSG_TEMPLATE.formatted(recipe.getId()));

        Mockito.verifyNoInteractions(recipeRepository);
    }

    @Test
    void shouldUpdateRecipeUseCase() {
        Recipe recipe = TestUtils.randomRecipe();
        Mockito.when(recipeOwnerRepository.getOwnerByRecipeId(recipe.getId())).thenReturn(Optional.of(TestUtils.randomRecipeOwner()));

        updateRecipeUseCase.execute(recipe);

        Mockito.verify(recipeRepository).update(recipe);
    }
}