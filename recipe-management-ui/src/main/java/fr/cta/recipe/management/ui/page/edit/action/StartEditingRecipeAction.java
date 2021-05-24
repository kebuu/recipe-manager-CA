package fr.cta.recipe.management.ui.page.edit.action;

import fr.cta.recipe.management.ui.action.AppAction;

import java.util.UUID;

public record StartEditingRecipeAction(UUID recipeId) implements AppAction {
}
