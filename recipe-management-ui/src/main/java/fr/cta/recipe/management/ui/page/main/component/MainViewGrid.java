package fr.cta.recipe.management.ui.page.main.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import fr.cta.recipe.management.ui.ActionDispatcher;
import fr.cta.recipe.management.ui.page.main.MainViewState;
import fr.cta.recipe.management.ui.page.main.RecipeView;
import fr.cta.recipe.management.ui.page.main.action.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainViewGrid extends Composite<Grid<RecipeView>> implements ActionDispatcher.StateChangeListener<MainViewState> {

    private final MainViewActionDispatcher actionDispatcher;

    public MainViewGrid(MainViewActionDispatcher actionDispatcher) {
        this.actionDispatcher = actionDispatcher;
        actionDispatcher.addStateChangeListener(this);
    }

    @Override
    protected Grid<RecipeView> initContent() {
        Grid<RecipeView> recipeGrid = new Grid<>(RecipeView.class, true);
        recipeGrid.addSelectionListener(selectionEvent -> {
            Optional<RecipeView> firstSelectedItem = selectionEvent.getFirstSelectedItem();
            firstSelectedItem.ifPresentOrElse(
                recipeView -> actionDispatcher.dispatchAction(new SelectRecipeInGridAction(recipeView.getId())),
                () -> actionDispatcher.dispatchAction(new RecipeUnselectedInGridAction()));
        });

        return recipeGrid;
    }

    @Override
    public void onStateChanged(ActionDispatcher.StateChangeEvent<MainViewState> stateChangeEvent) {
        MainViewState mainViewState = stateChangeEvent.newState();
        List<RecipeView> recipeViews = mainViewState.getRecipes().stream().map(recipe -> RecipeView.builder()
            .id(recipe.getId().toString())
            .name(recipe.getName())
            .nbOfSteps(recipe.getSteps().size())
            .build()
        ).collect(Collectors.toList());

        Grid<RecipeView> viewGrid = getContent();
        if (stateChangeEvent.propertyChanged(MainViewState::getRecipes)) {
            viewGrid.setItems(recipeViews);
        }

        Optional<RecipeView> selectedItem = Optional.ofNullable(mainViewState.getSelectedRecipeId())
            .flatMap(recipeId -> recipeViews.stream()
                .filter(recipeView -> recipeView.getId().equals(recipeId))
                .findFirst()
            );

        selectedItem.ifPresentOrElse(
            viewGrid::select,
            viewGrid::deselectAll
        );
    }
}
