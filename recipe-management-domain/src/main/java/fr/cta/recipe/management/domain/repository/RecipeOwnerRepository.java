package fr.cta.recipe.management.domain.repository;

import fr.cta.recipe.management.domain.entity.RecipeOwner;

import java.util.Optional;
import java.util.UUID;

public interface RecipeOwnerRepository {
    Optional<RecipeOwner> getOwnerById(UUID ownerId);

    Optional<RecipeOwner> getOwnerByRecipeId(UUID ownerId);
}
