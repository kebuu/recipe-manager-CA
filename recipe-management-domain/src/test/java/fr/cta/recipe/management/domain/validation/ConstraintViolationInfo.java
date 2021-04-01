package fr.cta.recipe.management.domain.validation;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Value
@AllArgsConstructor
public class ConstraintViolationInfo {

    Class<?> validatedType;
    String fieldPath;
    Class<?> constraintAnnotation;
    Object constraintAnnotationValue;

    public ConstraintViolationInfo(Class<?> validatedType, String fieldPath, Class<?> constraintAnnotation) {
        this(validatedType, fieldPath, constraintAnnotation, null);
    }

    @SuppressWarnings("rawtypes")
    public ConstraintViolationInfo(ConstraintViolation<?> constraintViolation) {
        validatedType = constraintViolation.getLeafBean().getClass();
        fieldPath = Objects.toString(constraintViolation.getPropertyPath(), StringUtils.EMPTY);
        constraintAnnotation = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType();
        constraintAnnotationValue = ((ConstraintDescriptorImpl) ((ConstraintViolationImpl) constraintViolation).getConstraintDescriptor()).getAnnotationDescriptor().getAttribute("value");
    }

    public static void assertConstraintViolationException(ConstraintViolationException constraintViolationException, ConstraintViolationInfo... constraintViolationInfos) {
        assertThat(constraintViolationException).isNotNull();
        assertThat(constraintViolationException.getConstraintViolations())
            .map(ConstraintViolationInfo::new)
            .containsExactlyInAnyOrder(constraintViolationInfos);
    }
}
