package fr.cta.recipe.management.domain.entity;

import lombok.*;
import org.apache.commons.collections4.ListUtils;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RecipeOwner extends Entity<UUID> {

    static final String DUPLICATED_RECIPE_NAME_ERROR_MSG_TEMPLATE = "There is already a recipe with name %s";
    static final String DUPLICATED_RECIPE_ID_EXCEPTION_MSG_TEMPLATE = "There is already a recipe with id %s";

    @NotBlank String name;
    List<RecipeInfo> recipeInfos;

    @Builder(toBuilder = true)
    public RecipeOwner(UUID id, String name, List<RecipeInfo> recipeInfos) {
        super(id);
        this.name = name;
        this.recipeInfos = Collections.unmodifiableList(ListUtils.emptyIfNull(recipeInfos));

        validate();
    }

    @Override
    protected void validate() {
        super.validate();

        validateRecipeNamesUniqueness();
        validateRecipeIdsUniqueness();
    }

    private void validateRecipeNamesUniqueness() {
        recipeInfos.stream()
            .collect(Collectors.groupingBy(RecipeInfo::getRecipeName, Collectors.counting()))
            .entrySet().stream()
            .filter(recipeNameOccurrence -> recipeNameOccurrence.getValue() > 1)
            .map(Map.Entry::getKey)
            .findAny()
            .ifPresent(duplicatedRecipeName -> {
                throw new IllegalStateException(DUPLICATED_RECIPE_NAME_ERROR_MSG_TEMPLATE.formatted(duplicatedRecipeName));
            });
    }

    private void validateRecipeIdsUniqueness() {
        recipeInfos.stream()
            .collect(Collectors.groupingBy(RecipeInfo::getRecipeId, Collectors.counting()))
            .entrySet().stream()
            .filter(recipeIdOccurrence -> recipeIdOccurrence.getValue() > 1)
            .map(Map.Entry::getKey)
            .findAny()
            .ifPresent(duplicatedRecipeId -> {
                throw new IllegalStateException(DUPLICATED_RECIPE_ID_EXCEPTION_MSG_TEMPLATE.formatted(duplicatedRecipeId));
            });
    }

    public RecipeOwner addRecipe(Recipe recipe) {
        List<RecipeInfo> newRecipeInfos = new ArrayList<>(recipeInfos);
        newRecipeInfos.add(new RecipeInfo(recipe.getId(), recipe.getName()));
        return toBuilder().recipeInfos(newRecipeInfos).build();
    }

    public RecipeOwner updateRecipe(Recipe recipe) {
        List<RecipeInfo> newRecipeInfos = recipeInfos.stream()
            .filter(recipeInfo -> !recipeInfo.getRecipeId().equals(recipe.getId()))
            .collect(Collectors.toList());

        newRecipeInfos.add(new RecipeInfo(recipe.getId(), recipe.getName()));
        return toBuilder().recipeInfos(newRecipeInfos).build();
    }

    @Value
    public static class RecipeInfo {
        UUID recipeId;
        String recipeName;
    }
}
