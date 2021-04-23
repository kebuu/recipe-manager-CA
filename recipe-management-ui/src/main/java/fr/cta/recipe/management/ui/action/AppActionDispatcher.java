package fr.cta.recipe.management.ui.action;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.ui.page.edit.EditRecipeView;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEdition;
import fr.cta.recipe.management.ui.page.home.HomeState;
import fr.cta.recipe.management.ui.page.home.HomeView;
import fr.cta.recipe.management.ui.page.home.action.*;

import java.util.Set;
import java.util.UUID;

public class AppActionDispatcher extends AbstractActionDispatcher<HomeState, AppAction> {

    private final GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase;

    public AppActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase) {
        this.getRecipesByOwnerIdUseCase = getRecipesByOwnerIdUseCase;
    }

    @Override
    protected HomeState initiateState() {
        return HomeState.builder()
            .recipes(Set.of())
            .build();
    }

    @Override
    protected HomeState doDispatchAction(HomeState mainViewState, AppAction appAction) {
        HomeState.HomeStateBuilder newStateBuilder = mainViewState.toBuilder();

        System.out.println("MainViewStateAction:" + appAction.getClass().getSimpleName());

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
        } else if (appAction instanceof SelectForEditionRecipeInGridAction selectForEditionRecipeInGridAction) {
            newStateBuilder.enabledEdition(true);
            UI.getCurrent().navigate(EditRecipeView.class, new RouteParameters(EditRecipeView.RECIPE_ID, selectForEditionRecipeInGridAction.selectedRecipeId()));
        } else if (appAction instanceof CancelRecipeEdition cancelRecipeEdition) {
            newStateBuilder.enabledEdition(false);
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(EditRecipeView.RECIPE_ID, cancelRecipeEdition.selectedRecipeId()));
        } else {
            throw new IllegalArgumentException("Unknown action type :" + appAction.getClass().getSimpleName());
        }

        return newStateBuilder.build();
    }
}
