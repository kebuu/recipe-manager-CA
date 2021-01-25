package fr.cta.recipe.management.domain.entity

import com.benasher44.uuid.Uuid
import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient

data class Recipe(
        val id: Uuid,
        val quantifiedIngredients: List<QuantifiedIngredient>,
        val steps: List<String>) {

    init {

    }

    constructor(quantifiedIngredients: List<QuantifiedIngredient>, steps: List<String>): this(Uuid.randomUUID(), quantifiedIngredients, steps)
}

