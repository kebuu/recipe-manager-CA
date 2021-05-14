package fr.cta.recipe.management.ui.action;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.*;
import fr.cta.recipe.management.ui.page.edit.*;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;
import fr.cta.recipe.management.ui.page.home.*;
import fr.cta.recipe.management.ui.page.home.action.*;

import java.util.*;

public class AppActionDispatcher extends AbstractActionDispatcher<AppState, AppAction> {

    private final GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final AddRecipeUseCase addRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;

    public AppActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase, UpdateRecipeUseCase updateRecipeUseCase, AddRecipeUseCase addRecipeUseCase, DeleteRecipeUseCase deleteRecipeUseCase) {
        this.getRecipesByOwnerIdUseCase = getRecipesByOwnerIdUseCase;
        this.updateRecipeUseCase = updateRecipeUseCase;
        this.addRecipeUseCase = addRecipeUseCase;
        this.deleteRecipeUseCase = deleteRecipeUseCase;
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

        if (appAction instanceof SelectRecipesInGridAction recipeSelectedInGridAction) {
            if (recipeSelectedInGridAction.selectedRecipeIds().size() == 1) {
                String selectedRecipeId = recipeSelectedInGridAction.selectedRecipeIds().get(0);
                newStateBuilder.selectedRecipeId(selectedRecipeId);
                String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class, new RouteParameters("recipeId", selectedRecipeId));
//                UI.getCurrent().getPage().getHistory().pushState(null, url);
                UI.getCurrent().navigate(url);
            } else {
                newStateBuilder.selectedRecipeId(null);
                String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class);
                UI.getCurrent().navigate(url);
//                UI.getCurrent().getPage().getHistory().pushState(null, url);
            }
        } else if (appAction instanceof GetAllRecipesAction) {
            Set<Recipe> recipes = getRecipesByOwnerIdUseCase.execute(UUID.randomUUID());
            newStateBuilder.clearRecipes();
            newStateBuilder.recipes(recipes);
            newStateBuilder.selectedRecipeId(null);
        } else if (appAction instanceof DeleteSelectedRecipesAction deleteSelectedRecipesAction) {
            deleteSelectedRecipesAction.selectedItems().forEach(
                recipeView -> deleteRecipeUseCase.execute(UUID.fromString(recipeView.getId()))
            );
            Set<Recipe> recipes = getRecipesByOwnerIdUseCase.execute(UUID.randomUUID());
            newStateBuilder.clearRecipes();
            newStateBuilder.recipes(recipes);
            newStateBuilder.selectedRecipeId(null);
        } else if (appAction instanceof SelectForEditionRecipeInGridAction selectForEditionRecipeInGridAction) {
            newStateBuilder.selectedRecipeId(selectForEditionRecipeInGridAction.selectedRecipeId());
            UI.getCurrent().navigate(CreateOrUpdateEditRecipeView.class, new RouteParameters(CreateOrUpdateEditRecipeView.RECIPE_ID, selectForEditionRecipeInGridAction.selectedRecipeId()));
        } else if (appAction instanceof CancelRecipeEditionAction cancelRecipeEdition) {
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(CreateOrUpdateEditRecipeView.RECIPE_ID, cancelRecipeEdition.selectedRecipeId()));
        } else if (appAction instanceof UpdateRecipeEditionAction saveRecipeEditionAction) {
            updateRecipeUseCase.execute(getState().getRecipeForEdition().toBuilder().name(saveRecipeEditionAction.recipeName()).build());
            String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class, new RouteParameters("recipeId", saveRecipeEditionAction.recipeId().toString()));
            UI ui = UI.getCurrent();
            ui.access(() -> ui.navigate(url, QueryParameters.simple(Map.of(HomeView.FORCE_REFRESH, Boolean.TRUE.toString()))));
        } else if (appAction instanceof CreateRecipeAction createRecipeEditionAction) {
            addRecipeUseCase.execute(new AddRecipeUseCase.Data(UUID.randomUUID(), Recipe.builder().id(UUID.randomUUID()).name(createRecipeEditionAction.recipeName()).build()));
            String url = RouteConfiguration.forSessionScope().getUrl(HomeView.class);
            UI ui = UI.getCurrent();
            ui.access(() -> ui.navigate(url, QueryParameters.simple(Map.of(HomeView.FORCE_REFRESH, Boolean.TRUE.toString()))));
        } else if (appAction instanceof StartRecipeCreationAction) {
            UI.getCurrent().navigate(CreateOrUpdateEditRecipeView.CREATE_RECIPE_ROUTE);
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
