package fr.cta.recipe.management.ui.page.home.bean;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RecipeView {
    String id;
    String name;
    int nbOfSteps;
}
