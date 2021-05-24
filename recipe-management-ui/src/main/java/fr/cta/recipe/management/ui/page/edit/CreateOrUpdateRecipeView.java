package fr.cta.recipe.management.ui.page.edit;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeCreationAction;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;
import fr.cta.recipe.management.ui.page.edit.action.StartEditingRecipeAction;

import java.util.UUID;

@Route("recipes/:" + CreateOrUpdateRecipeView.RECIPE_ID + "/edit")
@RouteAlias(CreateOrUpdateRecipeView.CREATE_RECIPE_ROUTE)
public class CreateOrUpdateRecipeView extends FormLayout implements BeforeEnterObserver {

	public static final String RECIPE_ID = "recipeId";
	public static final String CREATE_RECIPE_ROUTE = "recipes/new";
	private final AppActionDispatcher actionDispatcher;

	private UUID recipeId;
	private final TextField nameField;

	public CreateOrUpdateRecipeView(AppActionDispatcher actionDispatcher) {
		System.out.println(CreateOrUpdateRecipeView.class.getSimpleName());
		this.actionDispatcher = actionDispatcher;

		nameField = new TextField();
		nameField.setLabel("Name");
		nameField.setPlaceholder("Recipe name");

		HorizontalLayout actions = new HorizontalLayout();
		NativeButton save = new NativeButton("Save");
		Button reset = new Button("Reset");
		actions.add(save, reset);

		reset.addClickListener(event -> cancelAction());
		save.addClickListener(event -> saveRecipe(nameField));

		add(nameField, actions);

		Shortcuts.addShortcutListener(this,
			this::onEnterPressedOnForm,
			Key.ENTER)
			.listenOn(this);

		Shortcuts.addShortcutListener(reset,
			this::onEnterPressedOnReset,
			Key.ENTER)
			.listenOn(reset);

		actionDispatcher.addStateChangeListener(stateChangeEvent -> {
			nameField.setValue(stateChangeEvent.newState().getCurrentlyEditedRecipe().getName());
		});
	}

	private void cancelAction() {
		if (recipeId == null) {
			this.actionDispatcher.dispatchAction(new CancelRecipeCreationAction());
		} else {
			this.actionDispatcher.dispatchAction(new CancelRecipeEditionAction(recipeId.toString()));
		}
	}

	private void saveRecipe(TextField titleField) {
		if (recipeId == null) {
			this.actionDispatcher.dispatchAction(new CreateRecipeAction(titleField.getValue()));
		} else {
			this.actionDispatcher.dispatchAction(new UpdateRecipeEditionAction(recipeId, titleField.getValue()));
		}
	}

	private void onEnterPressedOnForm(ShortcutEvent event) {
		saveRecipe(nameField);
	}

	private void onEnterPressedOnReset() {
		cancelAction();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		beforeEnterEvent.getRouteParameters().get(RECIPE_ID)
			.ifPresent(recipeIdString -> {
				recipeId = UUID.fromString(recipeIdString);
				actionDispatcher.dispatchAction(new StartEditingRecipeAction(recipeId));
			});
	}
}