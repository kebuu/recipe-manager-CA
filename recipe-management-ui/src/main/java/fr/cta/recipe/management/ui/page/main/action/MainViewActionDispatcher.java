package fr.cta.recipe.management.ui.page.main.action;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.ui.ActionDispatcher;
import fr.cta.recipe.management.ui.page.main.MainView;
import fr.cta.recipe.management.ui.page.main.MainViewState;

import java.util.Set;
import java.util.UUID;

public class MainViewActionDispatcher extends ActionDispatcher<MainViewState, MainViewStateAction> {

    private final GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase;

    public MainViewActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase) {
        this.getRecipesByOwnerIdUseCase = getRecipesByOwnerIdUseCase;
    }

    @Override
    protected MainViewState initiateState() {
        return MainViewState.builder()
            .recipes(Set.of())
            .build();
    }

    @Override
    protected MainViewState doDispatchAction(MainViewState mainViewState, MainViewStateAction mainViewStateAction) {
        MainViewState.MainViewStateBuilder newStateBuilder = mainViewState.toBuilder();

        if (mainViewStateAction instanceof SelectRecipeInGridAction recipeSelectedInGridAction) {
            newStateBuilder.selectedRecipeId(recipeSelectedInGridAction.selectedRecipeId());
        } else if (mainViewStateAction instanceof RecipeUnselectedInGridAction) {
            newStateBuilder.selectedRecipeId(null);
        } else if (mainViewStateAction instanceof GetAllRecipesAction) {
            Set<Recipe> recipes = getRecipesByOwnerIdUseCase.execute(UUID.randomUUID());
            newStateBuilder.clearRecipes();
            newStateBuilder.recipes(recipes);
        } else {
            throw new IllegalArgumentException("Unknown action type :" + mainViewStateAction.getClass().getSimpleName());
        }

        MainViewState newState = newStateBuilder.build();
        String url = RouteConfiguration.forSessionScope().getUrl(MainView.class, new RouteParameters("recipeId", newState.getSelectedRecipeId()));
        UI.getCurrent().getPage().getHistory().pushState(null, url);

        return newState;
    }
}
