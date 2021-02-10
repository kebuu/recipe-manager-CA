package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.utils.TestUtils;
import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.catchThrowableOfType;

class RecipeTest {

    @Test
    void shouldCreateRecipe() {
        Assertions.assertThat(new Recipe(TestUtils.randomString(), List.of(), List.of()).getId()).isNotNull();
    }

    @Test
    void shouldValidateIngredientCreation() {
        ConstraintViolationException constraintViolationException = catchThrowableOfType(() -> new Recipe("", null, null),
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(Recipe.class, "name", NotBlank.class)
        );
    }

    @Test
    void shouldValidateEquality() {
        UUID uuid = UUID.randomUUID();
        Recipe recipe1 = new Recipe(
            uuid,
            RandomStringUtils.randomAlphabetic(5),
            List.of(),
            List.of()
        );

        Recipe recipe2 = new Recipe(
            uuid,
            RandomStringUtils.randomAlphabetic(6),
            List.of(),
            List.of()
        );

        Assertions.assertThat(recipe1)
            .isEqualTo(recipe2)
            .isNotSameAs(recipe2);
    }
}