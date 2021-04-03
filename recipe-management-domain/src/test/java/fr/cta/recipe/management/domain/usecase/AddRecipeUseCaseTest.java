package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.entity.RecipeOwner;
import fr.cta.recipe.management.domain.repository.RecipeOwnerRepository;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import fr.cta.recipe.management.domain.utils.RandomDomainUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AddRecipeUseCaseTest {

    @InjectMocks private AddRecipeUseCase addRecipeUseCase;
    @Mock private RecipeRepository recipeRepository;
    @Mock private RecipeOwnerRepository recipeOwnerRepository;

    @Test
    void shouldFailAddRecipeUseCase_missingOwner() {
        UUID ownerId = UUID.randomUUID();
        Recipe recipe = RandomDomainUtils.randomRecipe();
        AddRecipeUseCase.Data data = new AddRecipeUseCase.Data(
            ownerId,
            recipe
        );
        Mockito.when(recipeOwnerRepository.getOwnerById(ownerId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = Assertions.catchThrowableOfType(() -> addRecipeUseCase.execute(data), IllegalStateException.class);
        Assertions.assertThat(illegalStateException).hasMessage(AddRecipeUseCase.MISSING_OWNER_ERROR_MSG_TEMPLATE.formatted(ownerId));

        Mockito.verifyNoInteractions(recipeRepository);
    }

    @Test
    void shouldAddRecipeUseCase() {
        RecipeOwner recipeOwner = RandomDomainUtils.randomRecipeOwner();
        UUID ownerId = recipeOwner.getId();
        Recipe recipe = RandomDomainUtils.randomRecipe();
        AddRecipeUseCase.Data data = new AddRecipeUseCase.Data(
            ownerId,
            recipe
        );
        Mockito.when(recipeOwnerRepository.getOwnerById(ownerId)).thenReturn(Optional.of(recipeOwner));

        addRecipeUseCase.execute(data);

        Mockito.verify(recipeRepository).addRecipeToOwner(recipe, ownerId);
    }
}