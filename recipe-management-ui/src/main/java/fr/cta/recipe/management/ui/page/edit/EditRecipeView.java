package fr.cta.recipe.management.ui.page.edit;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;

import java.util.UUID;

@Route("recipes/:" + EditRecipeView.RECIPE_ID + "/edit")
public class EditRecipeView extends FormLayout implements BeforeEnterObserver {

	public static final String RECIPE_ID = "recipeId";
	private final AppActionDispatcher actionDispatcher;

	private UUID recipeId;

	public EditRecipeView(AppActionDispatcher actionDispatcher) {
		System.out.println(EditRecipeView.class.getSimpleName());
		this.actionDispatcher = actionDispatcher;

		TextField titleField = new TextField();
		titleField.setLabel("Name");
		titleField.setPlaceholder("Recipe name");

		HorizontalLayout actions = new HorizontalLayout();
		NativeButton save = new NativeButton("Save");
		NativeButton reset = new NativeButton("Reset");
		actions.add(save, reset);

		reset.addClickListener(event -> this.actionDispatcher.dispatchAction(new CancelRecipeEditionAction(recipeId.toString())));
		save.addClickListener(event -> this.actionDispatcher.dispatchAction(new SaveRecipeEditionAction(recipeId, titleField.getValue())));

		add(titleField, actions);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		beforeEnterEvent.getRouteParameters().get(RECIPE_ID)
			.ifPresent(recipeIdString -> recipeId = UUID.fromString(recipeIdString));
	}
}