package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.repository.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class GetRecipesByOwnerIdUseCaseTest {

    @InjectMocks private GetRecipesByOwnerIdUseCase getRecipesByOwnerUseCase;
    @Mock private RecipeRepository recipeRepository;

    @Test
    void shouldGetRecipeByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        Set<Recipe> recipes = Set.of();

        Mockito.when(recipeRepository.findByOwnerId(ownerId)).thenReturn(recipes);

        Assertions.assertThat(getRecipesByOwnerUseCase.execute(ownerId)).isSameAs(recipes);
    }
}