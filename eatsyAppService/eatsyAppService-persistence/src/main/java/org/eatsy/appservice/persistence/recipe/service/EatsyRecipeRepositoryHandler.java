package org.eatsy.appservice.persistence.recipe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class for utilising JPA repository methods and interacting with the database.
 * Tagged with Configuration, EnableJpaRepositories and EntityScan to ensure bean
 * created and spring Dependency Injection correctly take place.
 */
@Configuration
@EnableJpaRepositories
@EntityScan(basePackageClasses = RecipeEntity.class)
public class EatsyRecipeRepositoryHandler implements EatsyRecipeRepositoryService {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //The Eatsy Repository that extends the JPA interface
    @Autowired
    private EatsyRecipeRepository eatsyRecipeRepository;

    /**
     * Persists the RecipeEntity object to the database.
     * Calling this method on a recipe with a pre-existing ID will update the corresponding database record rather than insert a new one.
     *
     * @param recipeEntity the recipe to be persisted.
     * @return the recipeEntity that has been successfully persisted.
     */
    @Override
    public RecipeEntity persistRecipe(final RecipeEntity recipeEntity) {

        logger.debug("Persisting a recipe entity object called" + recipeEntity.getName());

        final RecipeEntity persistedRecipeEntity = eatsyRecipeRepository.save(recipeEntity);

        return persistedRecipeEntity;

    }

    /**
     * Retrieves all Recipe Entity objects that are stored in the Recipe table
     *
     * @return the list of all recipeEntity objects that are in the Recipe database table.
     */
    @Override
    public List<RecipeEntity> retrieveAllRecipes() {

        logger.debug("Retrieving all Recipe Entity objects from the Recipe DB table");

        final List<RecipeEntity> allRecipeEntities = eatsyRecipeRepository.findAll();

        return allRecipeEntities;

    }

    /**
     * Deletes the Recipe Entity object that is stored in the database with the specified unique key.
     */
    @Override
    public void deleteRecipeById(final String recipeKey) {

        logger.debug("Deleting Recipe Entity object from the Recipe database with recipeKey: " + recipeKey);

        eatsyRecipeRepository.deleteById(recipeKey);

    }

    //TODO
    @Override
    public Set<RecipeImageEntity> retrieveRecipeImageModels(final String recipeKey) {

        final Optional<RecipeEntity> recipeEntityOptional = eatsyRecipeRepository.findById(recipeKey);
        final RecipeEntity recipeEntity = recipeEntityOptional.orElse(null);
        final Set<RecipeImageEntity> recipeImageEntitySet = recipeEntity.getRecipeImageEntity();

        return recipeImageEntitySet;
    }


}
