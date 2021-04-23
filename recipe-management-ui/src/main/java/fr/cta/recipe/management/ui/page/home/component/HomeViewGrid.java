package fr.cta.recipe.management.ui.page.home.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import fr.cta.recipe.management.ui.action.AbstractActionDispatcher;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.home.HomeState;
import fr.cta.recipe.management.ui.page.home.action.*;
import fr.cta.recipe.management.ui.page.home.bean.RecipeView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HomeViewGrid extends Composite<Grid<RecipeView>> implements AbstractActionDispatcher.StateChangeListener<HomeState> {

    private final AppActionDispatcher actionDispatcher;

    public HomeViewGrid(AppActionDispatcher actionDispatcher) {
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

        recipeGrid.addItemDoubleClickListener(recipeViewItemDoubleClickEvent -> {
            System.out.println("recipeViewItemDoubleClickEvent");
            actionDispatcher.dispatchAction(new SelectForEditionRecipeInGridAction(recipeViewItemDoubleClickEvent.getItem().getId()));
        });

        return recipeGrid;
    }

    @Override
    public void onStateChanged(AbstractActionDispatcher.StateChangeEvent<HomeState> stateChangeEvent) {
        HomeState mainViewState = stateChangeEvent.newState();
        List<RecipeView> recipeViews = mainViewState.getRecipes().stream().map(recipe -> RecipeView.builder()
            .id(recipe.getId().toString())
            .name(recipe.getName())
            .nbOfSteps(recipe.getSteps().size())
            .build()
        ).collect(Collectors.toList());

        Grid<RecipeView> viewGrid = getContent();
        if (stateChangeEvent.propertyChanged(HomeState::getRecipes)) {
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
