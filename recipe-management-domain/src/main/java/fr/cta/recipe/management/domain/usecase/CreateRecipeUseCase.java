package fr.cta.recipe.management.domain.usecase;

import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient;
import lombok.Value;

import java.util.List;

public class CreateRecipeUseCase implements UseCase<CreateRecipeUseCase.CreateRecipeInput, CreateRecipeUseCase.CreateRecipeOutput> {

    @Override
    public CreateRecipeOutput execute(CreateRecipeInput input) {
        return null;
    }

    @Value
    public static class CreateRecipeInput {
        List<QuantifiedIngredient> quantifiedIngredients;
        List<String> steps;
    }

    public static class CreateRecipeOutput {
    }
}
