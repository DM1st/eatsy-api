package org.eatsy.appservice.persistence.image.service;

import org.eatsy.appservice.persistence.model.RecipeImageEntity;

/**
 * Interface for interacting with the JPA repository and persisting RecipeImageEntities
 */
public interface EatsyRecipeImageRepositoryService {

    /**
     * @param imageKey the unique identifier for the image
     * @return the recipeImageEntity with the supplied key
     */
    RecipeImageEntity findImageByKey(final String imageKey);

}
