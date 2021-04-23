package fr.cta.recipe.management.ui.page.home.component;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.cta.recipe.management.ui.action.AbstractActionDispatcher;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.home.HomeState;

public class RecipeStepsComponent extends Composite<VerticalLayout> implements AbstractActionDispatcher.StateChangeListener<HomeState> {

    private final VerticalLayout stepsContainer = new VerticalLayout();

    public RecipeStepsComponent(AppActionDispatcher actionDispatcher) {
        actionDispatcher.addStateChangeListener(this);
    }

    @Override
    protected VerticalLayout initContent() {
        VerticalLayout stepsLayout = new VerticalLayout();
        stepsLayout.add(new Text("Steps"), new Hr(), stepsContainer);
        return stepsLayout;
    }

    @Override
    public void onStateChanged(AbstractActionDispatcher.StateChangeEvent<HomeState> stateChangeEvent) {
        stepsContainer.removeAll();

        HomeState newState = stateChangeEvent.newState();
        Component[] stepComponents = newState.getRecipes().stream()
            .filter(recipe -> recipe.getId().toString().equals(newState.getSelectedRecipeId()))
            .findFirst()
            .stream()
            .flatMap(recipe -> recipe.getSteps().stream())
            .map(Paragraph::new)
            .toArray(Component[]::new);

        stepsContainer.add(stepComponents);
    }
}
