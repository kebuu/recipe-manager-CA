package fr.cta.recipe.management.domain.entity

import com.benasher44.uuid.Uuid
import fr.cta.recipe.management.domain.validation.Validation

data class IngredientUnit(
        override val id: Uuid,
        val name: String) : Entity(id) {

    init {
        Validation.validateNotBlank(::name)
    }

    constructor(name: String): this(Uuid.randomUUID(), name)
}