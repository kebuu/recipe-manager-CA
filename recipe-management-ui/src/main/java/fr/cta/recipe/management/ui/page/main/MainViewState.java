package fr.cta.recipe.management.ui.page.main;

import fr.cta.recipe.management.domain.entity.Recipe;
import lombok.*;

import java.util.Set;

@Value
@Builder(toBuilder = true)
public class MainViewState {

    @Singular Set<Recipe> recipes;
    String selectedRecipeId;
}
