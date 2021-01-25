package fr.cta.recipe.management.domain.mapper;

import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.CreateRecipeUseCase;
import org.mapstruct.Mapper;

@Mapper
public interface CreateRecipeRequestMapper {

    Recipe toRecipe(CreateRecipeUseCase.CreateRecipeInput createRecipeInput);
}
