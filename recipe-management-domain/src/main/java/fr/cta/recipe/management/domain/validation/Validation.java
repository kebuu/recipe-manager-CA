package fr.cta.recipe.management.domain.validation;

import javax.validation.*;
import java.util.Set;

public final class Validation {

    private static final Validator VALIDATOR = javax.validation.Validation.byDefaultProvider().configure()
        .buildValidatorFactory()
        .getValidator();

    public static <T> void validate(T beanToValidate) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(beanToValidate);

        if (!constraintViolations.isEmpty()) throw new IllegalStateException(constraintViolations.toString());
    }
}
