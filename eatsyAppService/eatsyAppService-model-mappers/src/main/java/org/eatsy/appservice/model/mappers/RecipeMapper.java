package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeMediaCardModel;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.model.RecipeEntity;

/**
 * Recipe Mapper to map between recipe domain, entity and model objects.
 */
public interface RecipeMapper {

    /**
     * Map the recipe domain object to a recipe model object.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeModel object that has been created from the recipe domain object.
     */
    RecipeModel mapDomainToModel(final Recipe recipe);

    /**
     * Map the recipeMediaCardModel to a recipe domain object.
     *
     * @param recipeMediaCardModel the model object to be mapped to domain object
     * @return the recipe domain object that has been created from the recipeMediaCard model object
     */
    Recipe mapModelToDomain(final RecipeMediaCardModel recipeMediaCardModel);

    /**
     * Map the recipe domain object to a recipe entity object for persistence to database.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeEntity object that has been created from the recipe domain object.
     */
    RecipeEntity mapDomainToEntity(final Recipe recipe);

    /**
     * Map the recipe entity object to a recipe domain object
     *
     * @param recipeEntity the entity object to be mapped
     * @return the recipeDomain object that has been created from the recipe entity object
     */
    Recipe mapEntityToDomain(final RecipeEntity recipeEntity);
}
