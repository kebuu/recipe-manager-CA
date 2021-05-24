package fr.cta.recipe.management.ui.page.home;

import fr.cta.recipe.management.domain.entity.Recipe;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class AppState {

    @Singular Set<Recipe> recipes;
    Recipe currentlyEditedRecipe;
    List<String> selectedRecipeIds;

    public AppState(Set<Recipe> recipes, Recipe currentlyEditedRecipe, List<String> selectedRecipeIds) {
        this.recipes = SetUtils.emptyIfNull(recipes);
        this.currentlyEditedRecipe = currentlyEditedRecipe;
        this.selectedRecipeIds = ListUtils.emptyIfNull(selectedRecipeIds);

        // todo validate state coherence
    }


    public boolean hasExactlyOneSelectedRecipe() {
        return selectedRecipeIds.size() == 1;
    }

    public Optional<Recipe> getUniqueSelectedRecipe() {
        Recipe uniqueSelectedRecipe = null;
        if (hasExactlyOneSelectedRecipe()) {
            uniqueSelectedRecipe = recipes.stream()
                                       .filter(recipe -> recipe.getId().toString().equals(selectedRecipeIds.get(0)))
                                       .findFirst()
                                       .orElseThrow();
        }
        return Optional.ofNullable(uniqueSelectedRecipe);
    }
}
