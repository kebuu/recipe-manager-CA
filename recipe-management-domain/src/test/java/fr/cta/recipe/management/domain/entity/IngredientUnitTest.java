package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

import java.util.UUID;

import static org.assertj.core.api.Assertions.catchThrowableOfType;

class IngredientUnitTest {

    @Test
    void shouldValidateIngredientUnitCreation() {
        ConstraintViolationException constraintViolationException = catchThrowableOfType(() -> new IngredientUnit(""),
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(IngredientUnit.class, "name", NotBlank.class)
        );
    }

    @Test
    void shouldValidateEquality() {
        UUID uuid = UUID.randomUUID();
        IngredientUnit ingredientUnit1 = new IngredientUnit(uuid, RandomStringUtils.randomAlphabetic(5));
        IngredientUnit ingredientUnit2 = new IngredientUnit(uuid, RandomStringUtils.randomAlphabetic(5));

        Assertions.assertThat(ingredientUnit1)
            .isEqualTo(ingredientUnit2)
            .isNotSameAs(ingredientUnit2);
    }
}