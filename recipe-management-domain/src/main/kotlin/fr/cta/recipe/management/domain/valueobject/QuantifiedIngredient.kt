package fr.cta.recipe.management.domain.valueobject

import fr.cta.recipe.management.domain.entity.Ingredient
import fr.cta.recipe.management.domain.entity.IngredientUnit
import fr.cta.recipe.management.domain.validation.Validation
import javax.validation.constraints.Min

data class QuantifiedIngredient(
        val ingredient: Ingredient,

        @field:Min(0)
        val quantity: Float,

        val unit: IngredientUnit) {

    init {
        Validation.validate(this)
    }
}