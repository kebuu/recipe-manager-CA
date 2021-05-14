package fr.cta.recipe.management.ui.page.edit;

import fr.cta.recipe.management.ui.action.AppAction;

import java.util.UUID;

public record UpdateRecipeEditionAction(UUID recipeId, String recipeName) implements AppAction {

}
