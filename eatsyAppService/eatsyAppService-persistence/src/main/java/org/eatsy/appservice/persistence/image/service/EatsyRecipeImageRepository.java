package org.eatsy.appservice.persistence.image.service;

import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository interface for CRUD operations on RecipeImageEntities in the Eatsy database
 */
@Repository
public interface EatsyRecipeImageRepository extends JpaRepository<RecipeImageEntity, String> {
}
