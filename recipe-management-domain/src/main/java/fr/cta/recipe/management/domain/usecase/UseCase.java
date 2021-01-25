package fr.cta.recipe.management.domain.usecase;

public interface UseCase<UseCaseInput, UseCaseOutput> {

    UseCaseOutput execute(UseCaseInput input) throws RuntimeException;
}
