package fr.cta.recipe.management.ui.action;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteParameters;
import fr.cta.recipe.management.domain.entity.Recipe;
import fr.cta.recipe.management.domain.usecase.*;
import fr.cta.recipe.management.ui.page.edit.CreateOrUpdateRecipeView;
import fr.cta.recipe.management.ui.page.edit.CreateRecipeAction;
import fr.cta.recipe.management.ui.page.edit.UpdateRecipeEditionAction;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeCreationAction;
import fr.cta.recipe.management.ui.page.edit.action.CancelRecipeEditionAction;
import fr.cta.recipe.management.ui.page.edit.action.StartEditingRecipeAction;
import fr.cta.recipe.management.ui.page.home.AppState;
import fr.cta.recipe.management.ui.page.home.action.DeleteSelectedRecipesAction;
import fr.cta.recipe.management.ui.page.home.HomeView;
import fr.cta.recipe.management.ui.page.home.action.LandingOnHomeViewAction;
import fr.cta.recipe.management.ui.page.home.action.SelectRecipesInGridAction;
import fr.cta.recipe.management.ui.page.home.action.StartRecipeCreationAction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AppActionDispatcher extends AbstractActionDispatcher<AppState, AppAction> {

    private final GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final AddRecipeUseCase addRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;
    private final GetRecipeByIdUseCase getRecipeByIdUseCase;

    public AppActionDispatcher(GetRecipesByOwnerIdUseCase getRecipesByOwnerIdUseCase, UpdateRecipeUseCase updateRecipeUseCase, AddRecipeUseCase addRecipeUseCase, DeleteRecipeUseCase deleteRecipeUseCase, GetRecipeByIdUseCase getRecipeByIdUseCase) {
        this.getRecipesByOwnerIdUseCase = getRecipesByOwnerIdUseCase;
        this.updateRecipeUseCase = updateRecipeUseCase;
        this.addRecipeUseCase = addRecipeUseCase;
        this.deleteRecipeUseCase = deleteRecipeUseCase;
        this.getRecipeByIdUseCase = getRecipeByIdUseCase;
    }

    @Override
    protected AppState initiateState() {
        return AppState.builder()
            .build();
    }

    @Override
    protected AppState doDispatchAction(AppState mainViewState, AppAction appAction) {
        AppState.AppStateBuilder newStateBuilder = mainViewState.toBuilder();

        System.out.println("AppAction:" + appAction.getClass().getSimpleName());

        if (appAction instanceof SelectRecipesInGridAction recipeSelectedInGridAction) {
            List<String> selectedRecipeIds = recipeSelectedInGridAction.selectedRecipeIds();
            newStateBuilder.selectedRecipeIds(selectedRecipeIds);
        } else if (appAction instanceof LandingOnHomeViewAction) {
            refreshRecipes(newStateBuilder);
            UI.getCurrent().navigate(HomeView.class);
        } else if (appAction instanceof DeleteSelectedRecipesAction deleteSelectedRecipesAction) {
            deleteSelectedRecipesAction.selectedItems().forEach(
                recipeView -> deleteRecipeUseCase.execute(UUID.fromString(recipeView.getId()))
            );
            refreshRecipes(newStateBuilder);
            UI.getCurrent().navigate(HomeView.class);
        } else if (appAction instanceof CancelRecipeEditionAction cancelRecipeEdition) {
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(CreateOrUpdateRecipeView.RECIPE_ID, cancelRecipeEdition.selectedRecipeId()));
        } else if (appAction instanceof CancelRecipeCreationAction) {
            UI.getCurrent().navigate(HomeView.class);
        } else if (appAction instanceof UpdateRecipeEditionAction saveRecipeEditionAction) {
            updateRecipeUseCase.execute(getState().getCurrentlyEditedRecipe().toBuilder()
                                            .name(saveRecipeEditionAction.recipeName())
                                            .build());

            refreshRecipes(newStateBuilder);
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(HomeView.RECIPE_ID, saveRecipeEditionAction.recipeId().toString()));
        } else if (appAction instanceof CreateRecipeAction createRecipeEditionAction) {
            UUID newRecipeId = UUID.randomUUID();
            addRecipeUseCase.execute(new AddRecipeUseCase.Data(UUID.randomUUID(), Recipe.builder().id(newRecipeId).name(createRecipeEditionAction.recipeName()).build()));
            refreshRecipes(newStateBuilder);
            UI.getCurrent().navigate(HomeView.class, new RouteParameters(HomeView.RECIPE_ID, newRecipeId.toString()));
        } else if (appAction instanceof StartRecipeCreationAction) {
            UI.getCurrent().navigate(CreateOrUpdateRecipeView.CREATE_RECIPE_ROUTE);
        } else if (appAction instanceof StartEditingRecipeAction startEditingRecipeAction) {
            Optional<Recipe> recipe = getRecipeByIdUseCase.execute(startEditingRecipeAction.recipeId());
            newStateBuilder.currentlyEditedRecipe(recipe.orElseThrow(() -> {
                // TODO
                return new RuntimeException("A mieux gerer en vrai");
            }));
        } else {
            throw new IllegalArgumentException("Unknown action type :" + appAction.getClass().getSimpleName());
        }

        return newStateBuilder.build();
    }

    private void refreshRecipes(AppState.AppStateBuilder newStateBuilder) {
        Set<Recipe> recipes = getRecipesByOwnerIdUseCase.execute(UUID.randomUUID());
        newStateBuilder.clearRecipes();
        newStateBuilder.recipes(recipes);
        newStateBuilder.selectedRecipeIds(List.of());
    }

    @Override
    protected boolean shouldFireChangeEvent(AppState newState) {
        return super.shouldFireChangeEvent(newState) || getState().getRecipes() != newState.getRecipes();
    }
}
