package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.ConstraintViolationInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class EntityTest {

    @Test
    void shouldFailCreatingInvalidEntity() {
        LocalEntity localEntity = new LocalEntity();

        ConstraintViolationException constraintViolationException = catchThrowableOfType(localEntity::validate,
            ConstraintViolationException.class);

        ConstraintViolationInfo.assertConstraintViolationException(constraintViolationException,
            new ConstraintViolationInfo(LocalEntity.class, "id", NotNull.class))
        ;
    }

    @Test
    void shouldCreateValidEntity() {
        assertThatCode(() -> new Entity<>(RandomStringUtils.randomAlphanumeric(10)) {}.validate())
            .doesNotThrowAnyException();
    }

    private static class LocalEntity extends Entity<String> {
        public LocalEntity() {
            super(null);
        }
    }
}