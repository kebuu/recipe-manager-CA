package fr.cta.recipe.management.ui.page.home;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.page.home.action.GetAllRecipesAction;
import fr.cta.recipe.management.ui.page.home.action.SelectRecipeInGridAction;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.home.component.HomeViewGrid;
import fr.cta.recipe.management.ui.page.home.component.RecipeStepsComponent;

@Route("recipes/:" + HomeView.RECIPE_ID + "?")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

	public static final String RECIPE_ID = "recipeId";
	public static final String FORCE_REFRESH = "forceRefresh";

	private final AppActionDispatcher actionDispatcher;

	public HomeView(AppActionDispatcher actionDispatcher) {
		System.out.println(HomeView.class.getSimpleName());

		this.actionDispatcher = actionDispatcher;

		HomeViewGrid mainViewGrid = new HomeViewGrid(actionDispatcher);
		RecipeStepsComponent recipeStepsComponent = new RecipeStepsComponent(actionDispatcher);

		add(mainViewGrid);
		add(recipeStepsComponent);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if (actionDispatcher.getState().getRecipes().isEmpty() || beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey(FORCE_REFRESH)) {
			actionDispatcher.dispatchAction(new GetAllRecipesAction());
		}

		beforeEnterEvent.getRouteParameters().get(RECIPE_ID)
			.ifPresent(recipeId -> actionDispatcher.dispatchAction(new SelectRecipeInGridAction(recipeId)));
	}
}