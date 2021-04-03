package fr.cta.recipe.management.ui.page.main.component;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.cta.recipe.management.ui.ActionDispatcher;
import fr.cta.recipe.management.ui.page.main.action.MainViewActionDispatcher;
import fr.cta.recipe.management.ui.page.main.MainViewState;

public class RecipeStepsComponent extends Composite<VerticalLayout> implements ActionDispatcher.StateChangeListener<MainViewState> {

    private final VerticalLayout stepsContainer = new VerticalLayout();

    public RecipeStepsComponent(MainViewActionDispatcher actionDispatcher) {
        actionDispatcher.addStateChangeListener(this);
    }

    @Override
    protected VerticalLayout initContent() {
        VerticalLayout stepsLayout = new VerticalLayout();
        stepsLayout.add(new Text("Steps"), new Hr(), stepsContainer);
        return stepsLayout;
    }

    @Override
    public void onStateChanged(ActionDispatcher.StateChangeEvent<MainViewState> stateChangeEvent) {
        stepsContainer.removeAll();

        MainViewState newState = stateChangeEvent.newState();
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
