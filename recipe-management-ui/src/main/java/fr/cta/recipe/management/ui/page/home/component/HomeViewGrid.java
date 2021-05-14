package fr.cta.recipe.management.ui.page.home.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.*;
import fr.cta.recipe.management.ui.action.*;
import fr.cta.recipe.management.ui.page.home.AppState;
import fr.cta.recipe.management.ui.page.home.HomeView;
import fr.cta.recipe.management.ui.page.home.action.SelectForEditionRecipeInGridAction;
import fr.cta.recipe.management.ui.page.home.action.SelectRecipesInGridAction;
import fr.cta.recipe.management.ui.page.home.bean.RecipeView;

import java.util.List;
import java.util.stream.Collectors;

public class HomeViewGrid extends Composite<Grid<RecipeView>> implements AbstractActionDispatcher.StateChangeListener<AppState, AppAction> {

    private final AppActionDispatcher actionDispatcher;

    public HomeViewGrid(AppActionDispatcher actionDispatcher) {
        this.actionDispatcher = actionDispatcher;
        actionDispatcher.addStateChangeListener(this);
        getContent().addDetachListener(event -> {
            System.out.println("addDetachListener");
            actionDispatcher.removeStateChangeListener(this);
        });
    }

    @Override
    protected Grid<RecipeView> initContent() {
        Grid<RecipeView> recipeGrid = new Grid<>(RecipeView.class, true);
        recipeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        recipeGrid.asMultiSelect().addSelectionListener(selectionEvent -> {
            if (selectionEvent.isFromClient()) {
                List<String> collect = selectionEvent.getAllSelectedItems().stream()
                    .map(RecipeView::getId)
                    .collect(Collectors.toList());

                actionDispatcher.dispatchAction(new SelectRecipesInGridAction(collect));
            }
        });

        recipeGrid.addItemDoubleClickListener(recipeViewItemDoubleClickEvent -> {
            System.out.println("recipeViewItemDoubleClickEvent");
            actionDispatcher.dispatchAction(new SelectForEditionRecipeInGridAction(recipeViewItemDoubleClickEvent.getItem().getId()));
        });

        recipeGrid.removeColumnByKey("name");
        recipeGrid.addComponentColumn(recipeView -> {
                RouterLink linkOnName = new RouterLink(
                    recipeView.getName(),
                    HomeView.class,
                    new RouteParameters("recipeId", recipeView.getId())
                );
                linkOnName.setHighlightCondition(HighlightConditions.locationPrefix("recipes/" + recipeView.getId()));
//                linkOnName.setHighlightCondition((routerLink, event) -> {
//                    System.out.println("toto");
//                    return true;
//                });
                linkOnName.setHighlightAction((routerLink, highlight) -> {
                    System.out.println("setHighlightAction");
                    if (highlight) {
                        routerLink.getElement().getStyle().set("font-weight", "bold");
                    } else {
                        routerLink.getElement().getStyle().remove("font-weight");
                    }
                });
                return linkOnName;
            }
        ).setHeader("name");

        return recipeGrid;
    }

    @Override
    public void onStateChanged(AbstractActionDispatcher.StateChangeEvent<AppState, AppAction> stateChangeEvent) {
        AppState mainViewState = stateChangeEvent.newState();
        List<RecipeView> recipeViews = mainViewState.getRecipes().stream().map(recipe -> RecipeView.builder()
            .id(recipe.getId().toString())
            .name(recipe.getName())
            .nbOfSteps(recipe.getSteps().size())
            .build()
        ).collect(Collectors.toList());

        Grid<RecipeView> viewGrid = getContent();
        if (!stateChangeEvent.hasForOriginAnyOf(SelectRecipesInGridAction.class)) {
            viewGrid.setItems(recipeViews);
        }

//        Optional<RecipeView> selectedItem = Optional.ofNullable(mainViewState.getSelectedRecipeId())
//            .flatMap(recipeId -> recipeViews.stream()
//                .filter(recipeView -> recipeView.getId().equals(recipeId))
//                .findFirst()
//            );
//
//        selectedItem.ifPresentOrElse(
//            viewGrid::select,
//            viewGrid::deselectAll
//        );
    }
}
