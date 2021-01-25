package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.Validation;
import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient;
import lombok.*;

import java.util.*;

@Value
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Recipe extends Entity {

    List<QuantifiedIngredient> quantifiedIngredients;
    List<String> steps;

    @Builder
    public Recipe(UUID id, List<QuantifiedIngredient> quantifiedIngredients, List<String> steps) {
        super(id);
        this.quantifiedIngredients = Collections.unmodifiableList(quantifiedIngredients);
        this.steps = Collections.unmodifiableList(steps);

        Validation.validate(this);
    }
}
