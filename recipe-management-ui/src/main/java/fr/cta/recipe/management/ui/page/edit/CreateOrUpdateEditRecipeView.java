package fr.cta.recipe.management.ui.page.edit;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;

import java.util.UUID;

@Route("recipes/:" + CreateOrUpdateEditRecipeView.RECIPE_ID + "/edit")
@RouteAlias(CreateOrUpdateEditRecipeView.CREATE_RECIPE_ROUTE)
public class CreateOrUpdateEditRecipeView extends FormLayout implements BeforeEnterObserver {

	public static final String RECIPE_ID = "recipeId";
	public static final String CREATE_RECIPE_ROUTE = "recipes/new";
	private final AppActionDispatcher actionDispatcher;

	private UUID recipeId;
	private final Button reset = new Button("Reset");

	public CreateOrUpdateEditRecipeView(AppActionDispatcher actionDispatcher) {
		System.out.println(CreateOrUpdateEditRecipeView.class.getSimpleName());
		this.actionDispatcher = actionDispatcher;

		TextField titleField = new TextField();
		titleField.setLabel("Name");
		titleField.setPlaceholder("Recipe name");

		HorizontalLayout actions = new HorizontalLayout();
		NativeButton save = new NativeButton("Save");
		actions.add(save, reset);

		reset.addClickListener(event -> this.actionDispatcher.dispatchAction(new CancelRecipeEditionAction(recipeId.toString())));
		save.addClickListener(saveRecipeEdition(titleField));

		add(titleField, actions);

		Shortcuts.addShortcutListener(this,
			this::onEnterPressedOnForm,
			Key.ENTER)
			.listenOn(this);

		Shortcuts.addShortcutListener(reset,
			this::onEnterPressedOnReset,
			Key.ENTER)
			.listenOn(reset);
	}

	private void onEnterPressedOnReset() {
		System.out.println("onEnterPressedOnReset");
	}

	private ComponentEventListener<ClickEvent<NativeButton>> saveRecipeEdition(TextField titleField) {
		if (recipeId == null) {
			return event -> this.actionDispatcher.dispatchAction(new CreateRecipeAction(titleField.getValue()));
		} else {
			return event -> this.actionDispatcher.dispatchAction(new UpdateRecipeEditionAction(recipeId, titleField.getValue()));
		}
	}

	private void onEnterPressedOnForm(ShortcutEvent event) {
		System.out.println("onEnterPressedOnForm");
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		beforeEnterEvent.getRouteParameters().get(RECIPE_ID)
			.ifPresent(recipeIdString -> recipeId = UUID.fromString(recipeIdString));
	}
}