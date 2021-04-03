package fr.cta.recipe.management.ui.page.main;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RecipeView {
    String id;
    String name;
    int nbOfSteps;
}
