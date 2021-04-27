package fr.cta.recipe.management.ui.action;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.domain.usecase.UpdateRecipeUseCase;
import fr.cta.recipe.management.ui.page.edit.EditRecipeView;
import fr.cta.recipe.management.ui.page.edit.SaveRecipeEditionAction;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;
import fr.cta.recipe.management.ui.page.home.AppState;
import fr.cta.recipe.management.ui.page.home.HomeView;
import fr.cta.recipe.management.ui.page.home.action.*;

import java.util.*;

public class AppActionDispatcher extends AbstractActionDispatcher<AppState, AppAction> {

    private final GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;

    public AppActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase, UpdateRecipeUseCase updateRecipeUseCase) {
        this.getRecipesByOwnerIdUseCase = getRecipesByOwnerIdUseCase;
        this.updateRecipeUseCase = updateRecipeUseCase;
    }

    @Override
    protected AppState initiateState() {
        return AppState.builder()
            .build();
    }

    @Override
    protected AppState doDispatchAction(AppState mainViewState, AppAction appAction) {
        AppState.AppStateBuilder newStateBuilder = mainViewState.toBuilder();

        System.out.println("AppAction:" + appAction.getClass().getSimpleName());

        if (appAction instanceof SelectRecipeInGridAction recipeSelectedInGridAction) {
            newStateBuilder.selectedRecipeId(recipeSelectedInGridAction.selectedRecipeId());
            String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class, new RouteParameters("recipeId", recipeSelectedInGridAction.selectedRecipeId()));
            UI.getCurrent().getPage().getHistory().pushState(null, url);
        } else if (appAction instanceof RecipeUnselectedInGridAction) {
            newStateBuilder.selectedRecipeId(null);
            String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class);
            UI.getCurrent().getPage().getHistory().pushState(null, url);
        } else if (appAction instanceof GetAllRecipesAction) {
            Set<Recipe> recipes = getRecipesByOwnerIdUseCase.execute(UUID.randomUUID());
            newStateBuilder.clearRecipes();
            newStateBuilder.recipes(recipes);
            newStateBuilder.selectedRecipeId(null);
        } else if (appAction instanceof SelectForEditionRecipeInGridAction selectForEditionRecipeInGridAction) {
            newStateBuilder.selectedRecipeId(selectForEditionRecipeInGridAction.selectedRecipeId());
            UI.getCurrent().navigate(EditRecipeView.class, new RouteParameters(EditRecipeView.RECIPE_ID, selectForEditionRecipeInGridAction.selectedRecipeId()));
        } else if (appAction instanceof CancelRecipeEditionAction cancelRecipeEdition) {
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(EditRecipeView.RECIPE_ID, cancelRecipeEdition.selectedRecipeId()));
        } else if (appAction instanceof SaveRecipeEditionAction saveRecipeEditionAction) {
            updateRecipeUseCase.execute(getState().getRecipeForEdition().toBuilder().name(saveRecipeEditionAction.recipeName()).build());
            String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class, new RouteParameters("recipeId", saveRecipeEditionAction.recipeId().toString()));
            UI ui = UI.getCurrent();
            ui.access(() -> ui.navigate(url, QueryParameters.simple(Map.of(HomeView.FORCE_REFRESH, Boolean.TRUE.toString()))));
        } else {
            throw new IllegalArgumentException("Unknown action type :" + appAction.getClass().getSimpleName());
        }

        return newStateBuilder.build();
    }

    @Override
    protected boolean shouldFireChangeEvent(AppState newState) {
        return super.shouldFireChangeEvent(newState) || getState().getRecipes() != newState.getRecipes();
    }
}
