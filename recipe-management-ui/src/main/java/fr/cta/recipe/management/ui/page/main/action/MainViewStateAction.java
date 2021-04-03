package fr.cta.recipe.management.ui.page.main.action;

public sealed interface MainViewStateAction
    permits
        SelectRecipeInGridAction,
        RecipeUnselectedInGridAction,
        GetAllRecipesAction
{
}
