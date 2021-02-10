package fr.cta.recipe.management.domain.utils;

import fr.cta.recipe.management.domain.entity.*;
import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.stream.*;

public final class TestUtils {

    public static Ingredient randomIngredient() {
        return new Ingredient(randomString());
    }

    public static IngredientUnit randomIngredientUnit() {
        return new IngredientUnit(randomString());
    }

    public static QuantifiedIngredient randomQuantifiedIngredient() {
        return new QuantifiedIngredient(randomIngredient(), RandomUtils.nextFloat(), randomIngredientUnit());
    }

    public static Recipe randomRecipe() {
        return new Recipe(
            UUID.randomUUID(),
            randomString(),
            List.of(randomQuantifiedIngredient(), randomQuantifiedIngredient()),
            randomListOfString()
        );
    }

    public static RecipeOwner randomRecipeOwner() {
        return new RecipeOwner(
            UUID.randomUUID(),
            randomString(),
            List.of(
                new RecipeOwner.RecipeInfo(UUID.randomUUID(), randomString()),
                new RecipeOwner.RecipeInfo(UUID.randomUUID(), randomString())
            )
        );
    }

    public static List<String> randomListOfString() {
        return IntStream.range(0, 2)
            .mapToObj(operand -> randomString())
            .collect(Collectors.toList());
    }

    public static String randomString() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
