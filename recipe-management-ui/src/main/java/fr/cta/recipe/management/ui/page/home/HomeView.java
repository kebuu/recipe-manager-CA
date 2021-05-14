package fr.cta.recipe.management.ui.page.home;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.home.action.*;
import fr.cta.recipe.management.ui.page.home.component.HomeViewGrid;
import fr.cta.recipe.management.ui.page.home.component.RecipeStepsComponent;

import java.util.Collections;

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

		Button createRecipeButton = new Button("Create recipe", new Icon(VaadinIcon.PLUS));
		createRecipeButton.addClickListener(event -> actionDispatcher.dispatchAction(new StartRecipeCreationAction()));

		Button deleteRecipeButton = new Button("Delete recipe", new Icon(VaadinIcon.TRASH));
		deleteRecipeButton.addClickListener(event -> actionDispatcher.dispatchAction(new DeleteSelectedRecipesAction(mainViewGrid.getContent().asMultiSelect().getSelectedItems())));
		add(new HorizontalLayout(createRecipeButton, deleteRecipeButton));
		add(mainViewGrid);
		add(recipeStepsComponent);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if (actionDispatcher.getState().getRecipes().isEmpty() || beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey(FORCE_REFRESH)) {
			actionDispatcher.dispatchAction(new GetAllRecipesAction());
		}

		beforeEnterEvent.getRouteParameters().get(RECIPE_ID)
			.ifPresent(recipeId -> actionDispatcher.dispatchAction(new SelectRecipesInGridAction(Collections.singletonList(recipeId))));
	}
}