package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.utils.TestUtils;
import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static org.assertj.core.api.Assertions.catchThrowableOfType;

class RecipeOwnerTest {

    @Test
    void shouldValidateRecipeOwnerCreation_constraintViolation() {
        ConstraintViolationException constraintViolationException = catchThrowableOfType(() -> new RecipeOwner(UUID.randomUUID(), "", null),
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(RecipeOwner.class, "name", NotBlank.class)
        );
    }

    @Test
    void shouldValidateRecipeOwnerCreation_duplicatedRecipeNames() {
        String duplicatedRecipeName = TestUtils.randomString();
        IllegalStateException illegalStateException = catchThrowableOfType(
            () -> new RecipeOwner(
                UUID.randomUUID(),
                TestUtils.randomString(),
                List.of(new RecipeOwner.RecipeInfo(UUID.randomUUID(), duplicatedRecipeName), new RecipeOwner.RecipeInfo(UUID.randomUUID(), duplicatedRecipeName))
            ),
            IllegalStateException.class
        );

        Assertions.assertThat(illegalStateException)
            .hasMessage(RecipeOwner.DUPLICATED_RECIPE_NAME_ERROR_MSG_TEMPLATE.formatted(duplicatedRecipeName));
    }

    @Test
    void shouldValidateRecipeOwnerCreation_duplicatedRecipeIds() {
        UUID duplicatedRecipeId = UUID.randomUUID();
        IllegalStateException illegalStateException = catchThrowableOfType(
            () -> new RecipeOwner(
                UUID.randomUUID(),
                TestUtils.randomString(),
                List.of(new RecipeOwner.RecipeInfo(duplicatedRecipeId, TestUtils.randomString()), new RecipeOwner.RecipeInfo(duplicatedRecipeId, TestUtils.randomString()))
            ),
            IllegalStateException.class
        );

        Assertions.assertThat(illegalStateException)
            .hasMessage(RecipeOwner.DUPLICATED_RECIPE_ID_EXCEPTION_MSG_TEMPLATE.formatted(duplicatedRecipeId));
    }

    @Test
    void shouldValidateEquality() {
        UUID uuid = UUID.randomUUID();

        RecipeOwner recipeOwner1 = new RecipeOwner(
            uuid,
            TestUtils.randomString(),
            List.of()
        );
        RecipeOwner recipeOwner2 = new RecipeOwner(
            uuid,
            TestUtils.randomString(),
            List.of()
        );

        Assertions.assertThat(recipeOwner1)
            .isEqualTo(recipeOwner2)
            .isNotSameAs(recipeOwner2);
    }

    @Test
    void shouldCreateRecipeOwner_emptyRecipes() {
        UUID uuid = UUID.randomUUID();
        String name = RandomStringUtils.randomAlphabetic(5);
        RecipeOwner recipeOwner = new RecipeOwner(
            uuid,
            name,
            null
        );

        Assertions.assertThat(recipeOwner.getName()).isEqualTo(name);
        Assertions.assertThat(recipeOwner.getRecipeInfos()).isEmpty();
    }

    @Test
    void shouldAddRecipe() {
        RecipeOwner recipeOwner = TestUtils.randomRecipeOwner();

        Recipe recipe = TestUtils.randomRecipe();
        RecipeOwner updatedOwner = recipeOwner.addRecipe(recipe);

        Assertions.assertThat(updatedOwner.getRecipeInfos())
            .hasSize(recipeOwner.getRecipeInfos().size() + 1)
            .containsAll(recipeOwner.getRecipeInfos())
            .contains(new RecipeOwner.RecipeInfo(recipe.getId(), recipe.getName()));
    }

    @Test
    void shouldUpdateRecipe() {
        RecipeOwner recipeOwner = TestUtils.randomRecipeOwner();
        int indexOfRecipeToUpdate = RandomUtils.nextInt(0, recipeOwner.getRecipeInfos().size());
        RecipeOwner.RecipeInfo existingRecipeInfo = recipeOwner.getRecipeInfos().get(indexOfRecipeToUpdate);

        Recipe updatedRecipe = TestUtils.randomRecipe().toBuilder()
            .id(existingRecipeInfo.getRecipeId())
            .build();

        RecipeOwner updatedOwner = recipeOwner.updateRecipe(updatedRecipe);

        List<RecipeOwner.RecipeInfo> newRecipeInfos = new ArrayList<>(recipeOwner.getRecipeInfos());
        newRecipeInfos.remove(indexOfRecipeToUpdate);

        Assertions.assertThat(updatedOwner.getRecipeInfos())
            .isNotEqualTo(recipeOwner.getRecipeInfos())
            .hasSize(recipeOwner.getRecipeInfos().size())
            .containsAll(newRecipeInfos)
            .contains(new RecipeOwner.RecipeInfo(updatedRecipe.getId(), updatedRecipe.getName()));
    }
}