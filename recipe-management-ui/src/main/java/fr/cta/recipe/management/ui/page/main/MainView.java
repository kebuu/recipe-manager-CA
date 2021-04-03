package fr.cta.recipe.management.ui.page.main;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.domain.usecase.GetRecipesByOwnerIdUseCase;
import fr.cta.recipe.management.ui.page.main.action.*;
import fr.cta.recipe.management.ui.page.main.component.MainViewGrid;
import fr.cta.recipe.management.ui.page.main.component.RecipeStepsComponent;

@Route("recipes/:recipeId?")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

	private final MainViewActionDispatcher actionDispatcher;

	public MainView(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase) {
		actionDispatcher = new MainViewActionDispatcher(getRecipesByOwnerIdUseCase);
		MainViewGrid mainViewGrid = new MainViewGrid(actionDispatcher);
		RecipeStepsComponent recipeStepsComponent = new RecipeStepsComponent(actionDispatcher);

		add(mainViewGrid);
		add(recipeStepsComponent);
		actionDispatcher.dispatchAction(new GetAllRecipesAction());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		System.out.println("beforeEnterEvent" + actionDispatcher);

		beforeEnterEvent.getRouteParameters().get("recipeId")
			.ifPresent(recipeId -> actionDispatcher.dispatchAction(new SelectRecipeInGridAction(recipeId)));
	}
}