package fr.cta.recipe.management.domain.usecase

import fr.cta.recipe.management.domain.entity.Recipe

class GetRecipeUseCase: UseCase<GetRecipeUseCase.GetRecipeRequest, Recipe> {
    class GetRecipeRequest {

    }

    override fun execute(useCaseData: GetRecipeRequest): Recipe {
        TODO("Not yet implemented")
    }
}