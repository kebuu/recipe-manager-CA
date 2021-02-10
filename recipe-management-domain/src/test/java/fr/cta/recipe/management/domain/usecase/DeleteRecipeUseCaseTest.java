package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeUseCaseTest {

    @InjectMocks private DeleteRecipeUseCase deleteRecipeUseCase;
    @Mock private RecipeRepository recipeRepository;

    @Test
    void shouldDeleteRecipeById() {
        UUID recipeId = UUID.randomUUID();

        deleteRecipeUseCase.execute(recipeId);

        Mockito.verify(recipeRepository).deleteById(recipeId);
    }
}