package fr.cta.recipe.management.ui.page.edit.action;

import fr.cta.recipe.management.ui.action.AppAction;

public record CancelRecipeEditionAction(String selectedRecipeId) implements AppAction {
}