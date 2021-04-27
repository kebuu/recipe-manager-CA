package fr.cta.recipe.management.ui.page.home;

import fr.cta.recipe.management.domain.entity.Recipe;
import lombok.*;

import java.util.Set;

@Value
@Builder(toBuilder = true)
public class AppState {

    @Singular Set<Recipe> recipes;
    String selectedRecipeId;

    public Recipe getRecipeForEdition() {
        return recipes.stream()
            .filter(recipe -> recipe.getId().toString().equals(selectedRecipeId))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No selected recipe for edition"));
    }
}