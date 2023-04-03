package org.eatsy.appservice.image.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.image.mappers.ImageMapper;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.persistence.image.service.EatsyRecipeImageRepositoryService;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * RecipeImageData Factory implementation
 * Tagged with @Component for Spring dependency injection
 */
@Component
public class ImageDataFactoryHandler implements ImageDataFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //RecipeImage Mapper implementation
    private final ImageMapper imageMapperHandler;

    //Repository handler for recipe image persistence
    private final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler;

    //Inject the dependency of the recipeImageMapper and repositoryImageHandler implementations into the RecipeImageDataFactoryHandler during instantiation.
    public ImageDataFactoryHandler(final ImageMapper imageMapperHandler, final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler) {
        this.imageMapperHandler = imageMapperHandler;
        this.eatsyRecipeImageRepositoryHandler = eatsyRecipeImageRepositoryHandler;
    }

    /**
     * Retrieves the image (in byte[] form) from the database
     *
     * @return The image as it is stored in the database (byte[] format).
     */
    @Override
    public byte[] retrieveImage(final String key) {

        logger.debug("Retrieving the requested recipeImageEntity");
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        //Get and return the image byte[] from this recipeImageEntity
        final byte[] recipeImage = recipeImageEntity.getPicByte();
        return recipeImage;
    }


    /**
     * Retrieves the image metadata for a given recipeImage
     *
     * @param key the ID of the image for a given recipe.
     * @return The image metadata, such as key, imageName and imageType (excluding the image itself).
     */
    @Override
    public ImageModel getImageData(final String key) {

        logger.debug("Retrieving the recipeImageData to return to the controller");

        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);

        //Map to domain to ensure entity recipeImage object is of allowed composition
        final RecipeImage recipeImage = imageMapperHandler.mapEntityToDomain(recipeImageEntity);

        //Map to model to be returned to the external service/API consumer via the controller.
        //This does not map the image byteArray as this is just method just returns the image metadata.
        final ImageModel imageModel = imageMapperHandler.mapDomanToModel(recipeImage);
        return imageModel;
    }

}
