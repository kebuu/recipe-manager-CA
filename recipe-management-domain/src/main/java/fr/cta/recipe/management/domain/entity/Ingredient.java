package fr.cta.recipe.management.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Ingredient extends Entity<UUID> {

    @NotBlank
    String name;

    public Ingredient(UUID id, String name) {
        super(id);
        this.name = name;

        validate();
    }

    public Ingredient(String name) {
        this(UUID.randomUUID(), name);
    }
}
