package fr.cta.recipe.management.ui.page.home.action;

import fr.cta.recipe.management.ui.action.AppAction;
import fr.cta.recipe.management.ui.page.home.bean.RecipeView;

import java.util.Set;

public record DeleteSelectedRecipesAction(Set<RecipeView> selectedItems) implements AppAction {

}
