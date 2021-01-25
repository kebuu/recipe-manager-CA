package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.Validation;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Ingredient extends Entity{

    String name;

    protected Ingredient(UUID id, String name) {
        super(id);
        this.name = name;

        Validation.validate(this);
    }
}
