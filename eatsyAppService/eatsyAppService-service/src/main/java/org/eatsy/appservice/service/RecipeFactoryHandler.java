package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.image.mappers.ImageMapper;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.model.RecipeMediaCardModel;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepositoryService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Recipe Factory implementation
 * Tagged with @Component for Spring dependency injection
 */
@Component
public class RecipeFactoryHandler implements RecipeFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //Recipe Mapper implementation
    private final RecipeMapper recipeMapperHandler;

    private final ImageMapper imageMapper;

    //Repository handler for recipe persistence
    private final EatsyRecipeRepositoryService eatsyRecipeRepositoryHandler;


    //Inject the dependency of the recipeMapper and repositoryHandler implementations into the RecipeFactoryHandler during instantiation.
    public RecipeFactoryHandler(final RecipeMapper recipeMapperHandler,
                                final EatsyRecipeRepositoryService eatsyRecipeRepositoryHandler,
                                final ImageMapper imageMapper) {
        this.recipeMapperHandler = recipeMapperHandler;
        this.eatsyRecipeRepositoryHandler = eatsyRecipeRepositoryHandler;
        this.imageMapper = imageMapper;
    }

    /**
     * Creates and persists a new Recipe.
     * These will be persisted via the Recipe Domain to ensure the model recipes are of allowed composition.
     *
     * @param recipeMediaCardModel the recipeMediaCard model that has the data (and media/image content) for the new Recipe
     * @return a recipe model object containing the non-media/image data from the newly created and persisted recipe.
     */
    @Override
    public RecipeModel createRecipe(final RecipeMediaCardModel recipeMediaCardModel) {

        RecipeModel newRecipeModel = null;

        //The recipeMediaCardModel to create a Recipe object must not be null and the recipeMediaCardModel must have a recipeName.
        if (null != recipeMediaCardModel
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getName().trim())
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getUploader().trim())
                && StringUtils.isNotEmpty(recipeMediaCardModel.getRecipeModel().getRecipeSummary().trim())) {

            logger.debug("Creating a new recipe domain object called " + recipeMediaCardModel.getRecipeModel().getName());

            //Map to domain to ensure requested recipe object is of allowed composition
            final Recipe recipe = recipeMapperHandler.mapModelToDomain(recipeMediaCardModel);

            logger.debug("Creating a new recipe entity object for persistence called " + recipe.getName());
            final RecipeEntity recipeEntity = recipeMapperHandler.mapDomainToEntity(recipe);

            //Persist the recipe to the database.
            final RecipeEntity persistedRecipeEntity = eatsyRecipeRepositoryHandler.persistRecipe(recipeEntity);

            //Map to domain to ensure entity recipe object is of allowed composition
            final Recipe persistedRecipeDomain = recipeMapperHandler.mapEntityToDomain(persistedRecipeEntity);

            //Map to model for disseminating back to the API consumer
            newRecipeModel = recipeMapperHandler.mapDomainToModel(persistedRecipeDomain);

        }
        return newRecipeModel;
    }

    /**
     * Retrieves all recipe model objects.
     * These will be mapped via the Recipe Domain to ensure the model recipes are of allowed composition.
     *
     * @return The list of all recipe model objects that exist.
     */
    @Override
    public List<RecipeModel> retrieveAllRecipes() {

        logger.debug("Retrieving all recipes to return to the controller");

        //Retrieve all RecipeEntity objects from the database.
        final List<RecipeEntity> allRecipeEntities = eatsyRecipeRepositoryHandler.retrieveAllRecipes();

        //Create a recipeModel list of all existing recipes to be disseminated back to the API consumer
        //(via the controller) by mapping the recipeEntities to recipeModels via the domain model.
        final List<RecipeModel> allRecipesModel = createRecipeModelList(allRecipeEntities);

        return allRecipesModel;

    }

    /**
     * Deletes the requested recipe and returns the updated list of recipes via the model
     *
     * @param recipeKey of the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeModelToDelete
     */
    @Override
    public List<RecipeModel> deleteRecipeAndReturnUpdatedRecipeList(final String recipeKey) {

        logger.debug("deleting recipe with key : " + recipeKey);

        //Delete the recipe with the specified recipeKey from the database.
        eatsyRecipeRepositoryHandler.deleteRecipeById(recipeKey);


        logger.debug("returning the updated list of all recipes via the model");
        final List<RecipeModel> allRecipesModel = retrieveAllRecipes();

        return allRecipesModel;
    }

    /**
     * Replaces the existing recipe with the updated version supplied.
     * These will be mapped via the Recipe Domain to ensure the model recipe is of allowed composition.
     *
     * @param recipeKey                       the unique ID of the recipe. This will allow the recipe that needs to be
     *                                        updated to be identified.
     * @param recipeMediaCardModelWithUpdates the recipeMediaCard model with the updated changes to be persisted.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    @Override
    public RecipeModel updateRecipe(final String recipeKey, final RecipeMediaCardModel recipeMediaCardModelWithUpdates) {

        logger.debug("replacing recipe with key: " + recipeKey + " for the new updated version");

        //Create the updated Recipe domain object to ensure the recipeModel object is of allowed composition
        final Recipe updatedRecipe = recipeMapperHandler.mapModelToDomain(recipeMediaCardModelWithUpdates);

        //Persist the updated recipe
        logger.debug("Creating a corresponding recipe entity object for persistence called " + updatedRecipe.getName());
        final RecipeEntity recipeEntityWithUpdates = recipeMapperHandler.mapDomainToEntity(updatedRecipe);

        final RecipeEntity persistedEntity = eatsyRecipeRepositoryHandler.persistRecipe(recipeEntityWithUpdates);

        //Map the updated recipeEntity to a RecipeModel
        //(via the domain model to ensure all recipe entity objects are of allowed composition) and return .
        final Recipe PersistedRecipeDomain = recipeMapperHandler.mapEntityToDomain(persistedEntity);
        final RecipeModel updatedRecipeModel = recipeMapperHandler.mapDomainToModel(PersistedRecipeDomain);
        return updatedRecipeModel;
    }

    //TODO
    @Override
    public Set<ImageModel> retrieveCorrespondingImageModels(final String recipeKey) {
        final Set<RecipeImageEntity> recipeImageEntities = eatsyRecipeRepositoryHandler.retrieveRecipeImageModels(recipeKey);
        final Set<ImageModel> imageModels = new HashSet<>();

        for(final RecipeImageEntity currentRecipeImageEntity :  recipeImageEntities){
            final RecipeImage recipeImage = imageMapper.mapEntityToDomain(currentRecipeImageEntity);
            final ImageModel imageModel = imageMapper.mapDomanToModel(recipeImage);
            imageModels.add(imageModel);
        }

        return imageModels;
    }


    /**
     * Creates a list of all Recipe Model objects to be returned to the controller from all RecipeEntites in the database.
     * These will be mapped via the Recipe Domain to ensure the model recipes are of allowed composition.
     *
     * @param allRecipeEntities the list of all recipe entities that exist in the recipe database table.
     * @return a list of all recipe model objects created from all recipe entities in the database.
     */
    private List<RecipeModel> createRecipeModelList(final List<RecipeEntity> allRecipeEntities) {

        logger.debug("Map the recipeEntites to domain objects, before creating and mapping to a recipeModel list");

        //a recipeModel list to be returned to the controller when all existing recipes have been added to this list.
        final List<RecipeModel> allRecipesModel = new ArrayList<>();

        //Map recipeEntities to the domain model to ensure all recipe entity objects are of allowed composition
        for (final RecipeEntity currentRecipeEntity : allRecipeEntities) {

            final Recipe currentDomainRecipe = recipeMapperHandler.mapEntityToDomain(currentRecipeEntity);

            //map the domain model to a recipeModel list to be disseminated back to the API consumer (via the controller)
            final RecipeModel currentModelRecipe = recipeMapperHandler.mapDomainToModel(currentDomainRecipe);
            allRecipesModel.add(currentModelRecipe);
        }

        logger.debug("returning the list of all recipe model objects.");

        return allRecipesModel;

    }

}



