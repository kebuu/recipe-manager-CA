package fr.cta.recipe.management.domain.usecase

import fr.cta.recipe.management.domain.entity.Recipe
import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient
import org.mapstruct.Mapper

class CreateRecipeUseCase: UseCase<CreateRecipeUseCase.CreateRecipeRequest, Recipe> {
    class CreateRecipeRequest(
            val quantifiedIngredients: List<QuantifiedIngredient>,
            val steps: List<String>,
        ) {
    }

    override fun execute(useCaseData: CreateRecipeRequest): Recipe {
        TODO("Not yet implemented")
    }
}