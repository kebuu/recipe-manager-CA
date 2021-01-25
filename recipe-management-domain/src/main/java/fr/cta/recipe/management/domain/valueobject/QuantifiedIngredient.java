package fr.cta.recipe.management.domain.valueobject;

import fr.cta.recipe.management.domain.entity.Ingredient;
import fr.cta.recipe.management.domain.entity.IngredientUnit;
import fr.cta.recipe.management.domain.validation.Validation;
import lombok.Value;

import javax.validation.constraints.Min;

@Value
public class QuantifiedIngredient {

    Ingredient ingredient;
    @Min(0) Float quantity;
    IngredientUnit unit;

    public QuantifiedIngredient(Ingredient ingredient, Float quantity, IngredientUnit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;

        Validation.validate(this);
    }
}
