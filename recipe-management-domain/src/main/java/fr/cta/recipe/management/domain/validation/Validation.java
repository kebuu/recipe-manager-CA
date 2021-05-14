package fr.cta.recipe.management.domain.validation;

import javax.validation.*;
import java.util.Set;

public interface Validation {

    String EXCEPTION_MESSAGE = "Bean validation error";

    Validator VALIDATOR = javax.validation.Validation.byDefaultProvider().configure()
        .buildValidatorFactory()
        .getValidator();

    static <T> void validate(T beanToValidate) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(beanToValidate);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(EXCEPTION_MESSAGE + ":" + constraintViolations.toString(), constraintViolations);
        }
    }
}
