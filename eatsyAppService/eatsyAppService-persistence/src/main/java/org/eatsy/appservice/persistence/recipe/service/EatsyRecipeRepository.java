package org.eatsy.appservice.persistence.recipe.service;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository interface for CRUD operations on RecipeEntities in the Eatsy database
 */
@Repository
public interface EatsyRecipeRepository extends JpaRepository<RecipeEntity, String> {


}
