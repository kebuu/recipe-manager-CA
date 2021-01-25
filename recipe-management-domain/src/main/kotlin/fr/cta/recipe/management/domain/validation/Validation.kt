package fr.cta.recipe.management.domain.validation

import java.lang.IllegalStateException
import javax.validation.Validation
import javax.validation.Validator
import kotlin.reflect.KProperty0

object Validation {

    private val validator: Validator = Validation.byDefaultProvider().configure()
            .buildValidatorFactory()
            .validator

    fun validateNotBlank(propertyUnderValidation: KProperty0<String>) {
        if(propertyUnderValidation.get().isBlank())
            throw IllegalArgumentException("Property ${propertyUnderValidation.name} should not be blank")
    }

    fun validate(beanToValidate: Any) {
        val constraintsViolations = validator.validate(beanToValidate)

        if (constraintsViolations.isNotEmpty()) throw IllegalStateException(constraintsViolations.toString())
    }
}