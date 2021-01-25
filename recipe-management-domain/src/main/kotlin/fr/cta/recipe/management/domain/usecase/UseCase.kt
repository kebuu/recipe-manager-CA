package fr.cta.recipe.management.domain.usecase

interface UseCase<UseCaseDataIn, UseCaseDataOut> {

    fun execute(useCaseData: UseCaseDataIn): UseCaseDataOut
}