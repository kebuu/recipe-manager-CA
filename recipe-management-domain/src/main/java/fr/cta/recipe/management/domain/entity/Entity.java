package fr.cta.recipe.management.domain.entity;

import fr.cta.recipe.management.domain.validation.Validation;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Entity<ID> {

    @NotNull
    @EqualsAndHashCode.Include
    protected final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    protected void validate() {
        Validation.validate(this);
    }
}
