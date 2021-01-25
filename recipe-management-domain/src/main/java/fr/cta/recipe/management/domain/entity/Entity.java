package fr.cta.recipe.management.domain.entity;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Entity {

    @EqualsAndHashCode.Include
    protected final UUID id;

    protected Entity(UUID id) {
        this.id = id;
    }
}
