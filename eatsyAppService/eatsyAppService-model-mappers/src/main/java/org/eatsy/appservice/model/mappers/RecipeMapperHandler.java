package org.eatsy.appservice.model.mappers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.model.RecipeMediaCardModel;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recipe Mapper Handler to map between recipe domain and model objects.
 */
@Component
public class RecipeMapperHandler implements RecipeMapper {

    private static final Logger logger = LogManager.getLogger(RecipeMapperHandler.class);

    /**
     * Map the recipe domain object to a recipe model object.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeModel object that has been created from the recipe domain object.
     */
    @Override
    public RecipeModel mapDomainToModel(final Recipe recipe) {

        RecipeModel recipeModel = null;
        //The recipe to be mapped (and it's fields) must not be null.
        if (null != recipe
                && StringUtils.isNotEmpty(recipe.getName().trim())
                && StringUtils.isNotEmpty(recipe.getUploader().trim())
                && StringUtils.isNotEmpty(recipe.getRecipeSummary().trim())) {

            logger.debug("Mapping domain object " + recipe.getName() + " to a recipeModel object");

            recipeModel = new RecipeModel();

            //Map key.
            recipeModel.setKey(recipe.getKey());

            //Map name.
            recipeModel.setName(recipe.getName());

            //Map uploader.
            recipeModel.setUploader(recipe.getUploader());

            //Map recipeSummary.
            recipeModel.setRecipeSummary(recipe.getRecipeSummary());

            //Map thumbsUpCount.
            recipeModel.setThumbsUpCount(recipe.getThumbsUpCount().toString());

            //Map thumbsDownCount.
            recipeModel.setThumbsDownCount(recipe.getThumbsDownCount().toString());

            //Map tags
            recipeModel.setTags(recipe.getTags());

            final Map<String, String> updatedStringIngredientsMap = recipe.getIngredients()
                    .entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));

            //Map set of ingredients.
            recipeModel.setIngredients(updatedStringIngredientsMap);

            final Map<String, String> updatedStringMethodMap = recipe.getMethod()
                    .entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
            //Map method.
            recipeModel.setMethod(updatedStringMethodMap);

        }
        return recipeModel;

    }

    /**
     * Map the recipeMediaCardModel to a recipe domain object.
     * If the model has an existing key, the mapper ensures the existing key is kept.
     * If the model doesn't yet have a key, then a new key will be assigned.
     *
     * @param recipeMediaCardModel the model object to be mapped to domain object
     * @return the recipe domain object that has been created from the recipe model object
     */
    @Override
    public Recipe mapModelToDomain(final RecipeMediaCardModel recipeMediaCardModel) {

        Recipe recipe = null;
        //The recipe model to be mapped must not be null, the recipeMediaCardModel must have a name, an uploader and a recipe summary
        if (null != recipeMediaCardModel
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getName().trim())
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getUploader().trim())
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getRecipeSummary().trim())) {

            logger.debug("Mapping model object " + recipeMediaCardModel.getRecipeModel().getName() + " to a recipe domain object");

            //TODO extract this into a mapper - MultipartFile -> RecipeImage
            final Set<RecipeImage> recipeImageSet = new HashSet<>();
            if (recipeMediaCardModel.getRecipeCardImages() != null) {

                for (final MultipartFile currentMultipartFile : recipeMediaCardModel.getRecipeCardImages()) {
                    try {
                        final RecipeImage recipeImage = new RecipeImage.RecipeImageBuilder(
                                currentMultipartFile.getName(),
                                currentMultipartFile.getContentType(),
                                currentMultipartFile.getBytes())
                                .build();

                        recipeImageSet.add(recipeImage);

                    } catch (final IOException e) {
                        logger.error("Error extracting multipart file.", e);
                    }
                }
            }

            final Map<Integer, String> updatedIntegerIngredientsMap = recipeMediaCardModel.getRecipeModel().getIngredients()
                    .entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), Map.Entry::getValue));

            final Map<Integer, String> updatedMethodIngredientsMap = recipeMediaCardModel.getRecipeModel().getMethod()
                    .entrySet().stream().collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), Map.Entry::getValue));

            final Recipe.RecipeBuilder recipeBuilder = new Recipe.RecipeBuilder(
                    recipeMediaCardModel.getRecipeModel().getName(),
                    recipeMediaCardModel.getRecipeModel().getUploader(),
                    recipeMediaCardModel.getRecipeModel().getRecipeSummary(),
                    recipeImageSet)
                    .withThumbsUpCount(Integer.parseInt(recipeMediaCardModel.getRecipeModel().getThumbsUpCount()))
                    .withThumbsDownCount(Integer.parseInt(recipeMediaCardModel.getRecipeModel().getThumbsDownCount()))
                    .withIngredients(updatedIntegerIngredientsMap)
                    .withMethod(updatedMethodIngredientsMap)
                    .withTags(recipeMediaCardModel.getRecipeModel().getTags());
            // The recipeBuilder automatically assigns a new key,
            // so if the model already has an existing key, then this will ensure the existing key is kept.
            if (recipeMediaCardModel.getRecipeModel().getKey() != null) {
                recipeBuilder.withSpecifiedKey(recipeMediaCardModel.getRecipeModel().getKey());
            }

            recipe = recipeBuilder.build();

        }

        return recipe;

    }

    /**
     * Map the recipe domain object to a recipe entity object for persistence to database.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeEntity object that has been created from the recipe domain object.
     */
    @Override
    public RecipeEntity mapDomainToEntity(final Recipe recipe) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name, uploader and summary.
        if (null != recipe
                && StringUtils.isNotEmpty(recipe.getName().trim())
                && StringUtils.isNotEmpty(recipe.getUploader().trim())
                && StringUtils.isNotEmpty(recipe.getRecipeSummary().trim())) {

            logger.debug("Mapping domain object " + recipe.getName() + " to a recipeEntity object");

            recipeEntity = new RecipeEntity();

            //Map key.
            recipeEntity.setKey(recipe.getKey());

            //Map name.
            recipeEntity.setName(recipe.getName());

            //Map uploader.
            recipeEntity.setUploader(recipe.getUploader());

            //Map recipeSummary.
            recipeEntity.setRecipeSummary(recipe.getRecipeSummary());

            //Map thumbsUpCount.
            recipeEntity.setThumbsUpCount(recipe.getThumbsUpCount());

            //Map thumbsDownCount.
            recipeEntity.setThumbsDownCount(recipe.getThumbsDownCount());

            //Map tags.
            recipeEntity.setTags(recipe.getTags());

            //Map set of ingredients.
            recipeEntity.setIngredientsMap(recipe.getIngredients());

            //Map method.
            recipeEntity.setMethodMap(recipe.getMethod());

        }
        return recipeEntity;
    }

    /**
     * Map the recipe entity object to a recipe domain object
     *
     * @param recipeEntity the entity object to be mapped
     * @return the recipeDomain object that has been created from the recipe entity object
     */
    @Override
    public Recipe mapEntityToDomain(final RecipeEntity recipeEntity) {

        Recipe recipe = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != recipeEntity
                && StringUtils.isNotEmpty(recipeEntity.getName().trim())
                && StringUtils.isNotEmpty(recipeEntity.getUploader().trim())
                && StringUtils.isNotEmpty(recipeEntity.getRecipeSummary().trim())) {

            logger.debug("Mapping entity object " + recipeEntity.getName() + " to a recipedomain object");

            //TODO extract this as a mapper
            final Set<RecipeImage> recipeImageSet = new HashSet<>();
            if (recipeEntity.getRecipeImageEntity() != null) {
                for (final RecipeImageEntity currentRecipeImageEntity : recipeEntity.getRecipeImageEntity()) {
                    final RecipeImage recipeImage = new RecipeImage.RecipeImageBuilder(
                            currentRecipeImageEntity.getImageName(),
                            currentRecipeImageEntity.getImageType(),
                            currentRecipeImageEntity.getPicByte())
                            .withSpecifiedKey(currentRecipeImageEntity.getKey())
                            .build();
                    recipeImageSet.add(recipeImage);
                }
            }

            recipe = new Recipe
                    .RecipeBuilder(recipeEntity.getName(), recipeEntity.getUploader(),
                    recipeEntity.getRecipeSummary(), recipeImageSet)
                    .withTags(recipeEntity.getTags())
                    .withIngredients(recipeEntity.getIngredientsMap())
                    .withMethod(recipeEntity.getMethodMap())
                    .withThumbsUpCount(recipeEntity.getThumbsUpCount()) //override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(recipeEntity.getThumbsDownCount()) //override newly generated 0 count to ensure no loss of data
                    .withSpecifiedKey(recipeEntity.getKey()) //override the newly generated key to ensure it is the same as the db entity key
                    .build();

        }
        return recipe;

    }
}
