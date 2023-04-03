package org.eatsy.appservice.image.mappers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;

/**
 * Image Mapper Handler to map between recipeImage domain and model objects.
 */
@Component
public class ImageMapperHandler implements ImageMapper {

    private static final Logger logger = LogManager.getLogger(ImageMapperHandler.class);

    /**
     * Map the recipe image entity object to a recipe image domain object
     *
     * @param recipeImageEntity the entity object to be mapped
     * @return the recipeImage domain object that has been created from the recipeImageEntity object
     */
    @Override
    public RecipeImage mapEntityToDomain(final RecipeImageEntity recipeImageEntity) {

        RecipeImage recipeImage = null;
        //The recipeImageEntity to be mapped (and it's fields) must not be null
        if (null != recipeImageEntity
                && StringUtils.isNotEmpty(recipeImageEntity.getKey().trim())
                && StringUtils.isNotEmpty(recipeImageEntity.getImageName().trim())
                && StringUtils.isNotEmpty(recipeImageEntity.getImageType().trim())
                && ArrayUtils.isNotEmpty(recipeImageEntity.getPicByte())) {

            logger.debug("Mapping entity object " + recipeImageEntity.getImageName() + " to a recipeImage domain object");

            recipeImage = new RecipeImage.RecipeImageBuilder(
                    recipeImageEntity.getImageName(),
                    recipeImageEntity.getImageType(),
                    recipeImageEntity.getPicByte())
                    .withSpecifiedKey(recipeImageEntity.getKey())//override the newly generated key to ensure it is the same as the db entity key
                    .build();

        }

        return recipeImage;

    }

    /**
     * Map the recipeImage domain object to a recipeImage model object.
     *
     * @param recipeImage the domain object to be mapped
     * @return the recipeImageModel object that has been created from the recipeImage domain object.
     */
    @Override
    public ImageModel mapDomanToModel(final RecipeImage recipeImage) {

        ImageModel imageModel = null;

        if (null != recipeImage
                && StringUtils.isNotEmpty(recipeImage.getKey().trim())
                && StringUtils.isNotEmpty(recipeImage.getImageName().trim())
                && StringUtils.isNotEmpty(recipeImage.getImageType().trim())
                && ArrayUtils.isNotEmpty(recipeImage.getPicByte())) {

            logger.debug("Mapping domain object " + recipeImage.getImageName() + " to a imageModel object");

            imageModel = new ImageModel();

            //Map key.
            imageModel.setKey(recipeImage.getKey());

            //Map name.
            imageModel.setImageName(recipeImage.getImageName());

            //Map imageType
            imageModel.setImageType(recipeImage.getImageType());

            //Mapping the byte[] is not done as this is not included in the imageModel since it is
            //returned separately via the /get/{imageKey} endpoint

        }

        return imageModel;

    }
}
