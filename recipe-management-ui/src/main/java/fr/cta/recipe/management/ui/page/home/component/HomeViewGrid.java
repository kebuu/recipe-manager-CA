package fr.cta.recipe.management.ui.page.home.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import fr.cta.recipe.management.ui.action.AbstractActionDispatcher;
import fr.cta.recipe.management.ui.action.AppAction;
import fr.cta.recipe.management.ui.action.AppActionDispatcher;
import fr.cta.recipe.management.ui.page.edit.CreateOrUpdateRecipeView;
import fr.cta.recipe.management.ui.page.home.AppState;
import fr.cta.recipe.management.ui.page.home.action.DeleteRecipeAction;
import fr.cta.recipe.management.ui.page.home.action.SelectRecipesInGridAction;
import fr.cta.recipe.management.ui.page.home.bean.RecipeView;

import java.util.List;
import java.util.Map;
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
            UI.getCurrent().navigate(CreateOrUpdateRecipeView.class, new RouteParameters(Map.of(CreateOrUpdateRecipeView.RECIPE_ID, recipeViewItemDoubleClickEvent.getItem().getId())));
        });

        recipeGrid.removeColumnByKey("name");
        recipeGrid.addComponentColumn(recipeView -> {
                RouterLink linkOnName = new RouterLink(
                    recipeView.getName(),
                    CreateOrUpdateRecipeView.class,
                    new RouteParameters("recipeId", recipeView.getId())
                );
                linkOnName.setHighlightCondition(HighlightConditions.locationPrefix("recipes/" + recipeView.getId()));
                linkOnName.setHighlightAction((routerLink, highlight) -> {
                    if (highlight) {
                        routerLink.getElement().getStyle().set("font-weight", "bold");
                    } else {
                        routerLink.getElement().getStyle().remove("font-weight");
                    }
                });
                return linkOnName;
            }
        ).setHeader("name");

        GridContextMenu<RecipeView> recipeViewGridContextMenu = recipeGrid.addContextMenu();
        recipeViewGridContextMenu.addGridContextMenuOpenedListener(event -> {
            if (event.getItem().isEmpty()) {
                recipeViewGridContextMenu.close();
            }
        });
        recipeViewGridContextMenu.addItem("Edit", event -> UI.getCurrent().navigate(CreateOrUpdateRecipeView.class, new RouteParameters(Map.of(CreateOrUpdateRecipeView.RECIPE_ID, event.getItem().orElseThrow().getId()))));
        recipeViewGridContextMenu.addItem("Delete", event -> actionDispatcher.dispatchAction(new DeleteRecipeAction(event.getItem().orElseThrow().getId())));

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
        if (stateChangeEvent.hasNotForOriginAnyOf(SelectRecipesInGridAction.class)) {
            viewGrid.setItems(recipeViews);
        }
    }
}
