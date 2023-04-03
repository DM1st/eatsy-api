package org.eatsy.appservice.persistence.image.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * Class for utilising JPA repository methods and interacting with the database.
 * Tagged with Configuration, EnableJpaRepositories and EntityScan to ensure bean
 * created and spring Dependency Injection correctly take place.
 */
@Configuration
@EnableJpaRepositories
@EntityScan(basePackageClasses = RecipeImageEntity.class)
public class EatsyRecipeImageRepositoryHandler implements EatsyRecipeImageRepositoryService {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //The Eatsy Repository that extends the JPA interface
    @Autowired
    private EatsyRecipeImageRepository eatsyRecipeImageRepository;

    /**
     * @param imageKey the unique identifier for the image
     * @return the recipeImageEntity with the supplied key
     */
    @Override
    public RecipeImageEntity findImageByKey(final String imageKey) {

        final RecipeImageEntity recipeImageEntity;

        logger.debug("Attempting to retrieve recipeImage with key " + imageKey);

        //If the entity is present then return it, otherwise assign null and log the error
        final Optional<RecipeImageEntity> recipeImageEntityOptional = eatsyRecipeImageRepository.findById(imageKey);
        recipeImageEntity = recipeImageEntityOptional.orElse(null);
        if (recipeImageEntity == null) {
            logger.error("RecipeImage with key " + imageKey + " could not be retrieved");
        }

        return recipeImageEntity;
    }
}
