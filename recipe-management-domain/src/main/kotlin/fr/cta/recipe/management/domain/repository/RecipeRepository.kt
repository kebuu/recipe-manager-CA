package fr.cta.recipe.management.domain.repository

import com.benasher44.uuid.Uuid
import fr.cta.recipe.management.domain.entity.Recipe

interface RecipeRepository {

    fun getById(recipeId: Uuid): Recipe

    fun insert(recipe: Recipe): Unit

    fun update(recipe: Recipe): Unit

    fun deleteById(recipeId: Uuid): Unit
}