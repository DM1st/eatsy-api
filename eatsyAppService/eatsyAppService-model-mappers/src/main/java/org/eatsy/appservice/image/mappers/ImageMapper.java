package org.eatsy.appservice.image.mappers;

import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;

/**
 * Image Mapper to map between image domain, entity and model objects.
 */
public interface ImageMapper {

    /**
     * Map the recipe image entity object to a recipe image domain object
     *
     * @param recipeImageEntity the entity object to be mapped
     * @return the recipeImage domain object that has been created from the recipeImageEntity object
     */
    RecipeImage mapEntityToDomain(final RecipeImageEntity recipeImageEntity);

    /**
     * Map the recipeImage domain object to a recipeImage model object.
     *
     * @param recipeImage the domain object to be mapped
     * @return the recipeImageModel object that has been created from the recipeImage domain object.
     */
    ImageModel mapDomanToModel(final RecipeImage recipeImage);

}
