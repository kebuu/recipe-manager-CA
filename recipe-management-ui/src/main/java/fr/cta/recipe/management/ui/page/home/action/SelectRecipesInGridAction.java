package fr.cta.recipe.management.ui.page.home.action;

import fr.cta.recipe.management.ui.action.AppAction;

import java.util.List;

public record SelectRecipesInGridAction(List<String> selectedRecipeIds) implements AppAction {

}
