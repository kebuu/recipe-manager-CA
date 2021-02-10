package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

import static org.assertj.core.api.Assertions.catchThrowableOfType;

class IngredientTest {

    @Test
    void shouldValidateIngredientCreation() {
        ConstraintViolationException constraintViolationException = catchThrowableOfType(() -> new Ingredient(""),
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(Ingredient.class, "name", NotBlank.class)
        );
    }

    @Test
    void shouldValidateEquality() {
        UUID uuid = UUID.randomUUID();
        Ingredient ingredient1 = new Ingredient(uuid, RandomStringUtils.randomAlphabetic(5));
        Ingredient ingredient2 = new Ingredient(uuid, RandomStringUtils.randomAlphabetic(5));

        Assertions.assertThat(ingredient1)
            .isEqualTo(ingredient2)
            .isNotSameAs(ingredient2);
    }
}