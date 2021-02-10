package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.Validation;
import fr.cta.recipe.management.domain.valueobject.QuantifiedIngredient;
import lombok.*;
import org.apache.commons.collections4.ListUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Value
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Recipe extends Entity<UUID> {

    @NotBlank String name;
    @NotNull List<QuantifiedIngredient> quantifiedIngredients;
    @NotNull List<String> steps;

    @Builder(toBuilder = true)
    public Recipe(UUID id, String name, List<QuantifiedIngredient> quantifiedIngredients, List<String> steps) {
        super(id);
        this.name = name;
        this.quantifiedIngredients = ListUtils.emptyIfNull(quantifiedIngredients);
        this.steps = ListUtils.emptyIfNull(steps);

        Validation.validate(this);
    }

    public Recipe(String name, List<QuantifiedIngredient> quantifiedIngredients, List<String> steps) {
        this(UUID.randomUUID(), name, quantifiedIngredients, steps);
    }
}
