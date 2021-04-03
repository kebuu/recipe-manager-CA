package fr.cta.recipe.management.domain.valueobject;

import fr.cta.recipe.management.domain.entity.Ingredient;
import fr.cta.recipe.management.domain.entity.IngredientUnit;
import fr.cta.recipe.management.domain.utils.RandomDomainUtils;
import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.catchThrowableOfType;

class QuantifiedIngredientTest {

    @Test
    void shouldValidateEntity() {
        ConstraintViolationException constraintViolationException = catchThrowableOfType(() -> new QuantifiedIngredient(null, -1F, null),
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(QuantifiedIngredient.class, "ingredient", NotNull.class),
            new ConstraintViolationInfo(QuantifiedIngredient.class, "quantity", Min.class, 0L),
            new ConstraintViolationInfo(QuantifiedIngredient.class, "unit", NotNull.class)
        );
    }


    @Test
    void shouldValidateEquality() {
        Ingredient ingredient = RandomDomainUtils.randomIngredient();
        float quantity = RandomUtils.nextFloat();
        IngredientUnit unit = RandomDomainUtils.randomIngredientUnit();
        QuantifiedIngredient quantifiedIngredient1 = new QuantifiedIngredient(ingredient, quantity, unit);
        QuantifiedIngredient quantifiedIngredient2 = new QuantifiedIngredient(ingredient, quantity, unit);

        Assertions.assertThat(quantifiedIngredient1)
            .isEqualTo(quantifiedIngredient2)
            .isNotSameAs(quantifiedIngredient2);
    }
}