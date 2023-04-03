package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepositoryService;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for service layer unit tests to create the required mockito mocks via these mock creation utility methods.
 */
public interface RecipeMockFactory {

    /**
     * Only the Service module is under test in the service unit tests.
     * The Mapper and Persistence modules that the Recipe Factory interacts with need to be mocked to ensure
     * these tests are RecipeFactory unit tests.
     * This method creates mocks for the relevant EatsyRepository and RecipeMapper services when the create or update
     * service methods are called
     *
     * @param inputRecipeModel randomly generated recipe model test data.
     */
    static RecipeEntity createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(
            final RecipeModel inputRecipeModel, final EatsyRecipeRepositoryService eatsyRepositoryHandler, final RecipeMapper recipeMapperHandler) {

        //Configure the eatsyRepository Mock to return the mocked data (persisted Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory.createMockRecipeEntityFromModel(inputRecipeModel);
        Mockito.when(eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapper mock to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = RecipeMockFactory.createMockDomainRecipeFromModel(inputRecipeModel);
        Mockito.when(recipeMapperHandler.mapModelToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapEntityToDomain(mockedPersistedRecipeEntity)).thenReturn(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(inputRecipeModel);

        return mockedPersistedRecipeEntity;
    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the relevant EatsyRepository and RecipeMapper services when the retrieveAllRecipe
     * service method is called.
     *
     * @param expectedRecipeModelList randomly generated list of recipe model test data.
     */
    static void createMocksForMapperAndPersistenceServicesInRetrieveAllRecipeServiceMethod(
            final List<RecipeModel> expectedRecipeModelList, final EatsyRecipeRepositoryService eatsyRepositoryHandler, final RecipeMapper recipeMapperHandler) {

        //Mock the response for when the service under test calls the persistence layer eatsyRepositoryHandler.retrieveAllRecipes() method.
        final List<RecipeEntity> expectedRecipeEntityList = RecipeMockFactory.createMockRecipeEntityListFromModelList(expectedRecipeModelList);
        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);
        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapEntityToDomain(testEntity) method for a given test entity.
        final List<Recipe> expectedRecipeList = RecipeMockFactory.createMockDomainRecipesFromEntityRecipes(expectedRecipeEntityList);
        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdatedDomainRecipe = RecipeMockFactory.createMockRecipeFromEntity(currentEntity);
            Mockito.when(recipeMapperHandler.mapEntityToDomain(currentEntity)).thenReturn(expectedUpdatedDomainRecipe);
        }
        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapDomainToModel(testRecipe) method for a given test entity.
        for (final Recipe currentRecipe : expectedRecipeList) {
            final RecipeModel expectedRecipeModel = RecipeMockFactory.createMockRecipeModelFromDomain(currentRecipe);
            Mockito.when(recipeMapperHandler.mapDomainToModel(currentRecipe)).thenReturn(expectedRecipeModel);
        }
    }

    /*
    Methods for the above Mock-factory-setup-helpers below:
     */

    /**
     * Mocks the recipeMapper service interactions when a list of Recipe Entities would need to be mapped to a list of domain recipes.
     *
     * @param expectedRecipeEntityList randomly generated recipe entity list test data.
     */
    static List<Recipe> createMockDomainRecipesFromEntityRecipes(final List<RecipeEntity> expectedRecipeEntityList) {

        final List<Recipe> expectedDomainRecipes = new ArrayList<>();

        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdateDomainRecipe = createMockRecipeFromEntity(currentEntity);
            expectedDomainRecipes.add(expectedUpdateDomainRecipe);
        }
        return expectedDomainRecipes;
    }

    /**
     * Creates a Recipe object from a Recipe Entity object
     *
     * @param inputRecipeEntity recipe model object.
     * @return A Recipe object.
     */
    static Recipe createMockRecipeFromEntity(final RecipeEntity inputRecipeEntity) {

        Recipe recipe = null;
        //The recipeEntity to be mapped must not be null and the recipeEntity must have a name, uploader and summary.
        if (null != inputRecipeEntity
                && StringUtils.isNotEmpty(inputRecipeEntity.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeEntity.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeEntity.getRecipeSummary().trim())) {

            recipe = new Recipe
                    .RecipeBuilder(
                    inputRecipeEntity.getName(),
                    inputRecipeEntity.getUploader(),
                    inputRecipeEntity.getRecipeSummary())
                    .withTags(inputRecipeEntity.getTags())
                    .withIngredients(inputRecipeEntity.getIngredientsMap())
                    .withMethod(inputRecipeEntity.getMethodMap())
                    .withThumbsUpCount(inputRecipeEntity.getThumbsUpCount())//override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(inputRecipeEntity.getThumbsDownCount())//override newly generated 0 count to ensure no loss of data
                    .withSpecifiedKey(inputRecipeEntity.getKey())
                    .build();
        }
        return recipe;

    }

    /**
     * Creates a Recipe Domain object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Domain object.
     */
    static Recipe createMockDomainRecipeFromModel(final RecipeModel inputRecipeModel) {

        Recipe domainRecipe = null;
        //The recipe model to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel
                && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getRecipeSummary().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(
                    inputRecipeModel.getName(),
                    inputRecipeModel.getUploader(),
                    inputRecipeModel.getRecipeSummary())
                    .withTags(inputRecipeModel.getTags())
                    .withThumbsUpCount(inputRecipeModel.getThumbsUpCount()) //override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(inputRecipeModel.getThumbsDownCount()) //override newly generated 0 count to ensure no loss of data
                    .withIngredients(inputRecipeModel.getIngredients())
                    .withMethod(inputRecipeModel.getMethod())
                    .build();

        }
        return domainRecipe;

    }

    /**
     * Creates a Recipe Model object from a Recipe Domain object that has been added to the recipeCache
     *
     * @param mockedDomainRecipe recipe domain object.
     * @return A Recipe Model object.
     */
    static RecipeModel createMockRecipeModelFromDomain(final Recipe mockedDomainRecipe) {

        RecipeModel recipeModel = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != mockedDomainRecipe && StringUtils.isNotEmpty(mockedDomainRecipe.getName().trim())) {

            recipeModel = new RecipeModel();
            //Map key.
            recipeModel.setKey(mockedDomainRecipe.getKey());
            //Map name.
            recipeModel.setName(mockedDomainRecipe.getName());
            //Map uploader
            recipeModel.setUploader(mockedDomainRecipe.getUploader());
            //Map recipe summary
            recipeModel.setRecipeSummary(mockedDomainRecipe.getRecipeSummary());
            //Map thumbsUp count
            recipeModel.setThumbsUpCount(mockedDomainRecipe.getThumbsUpCount());
            //Map thumbsDown count
            recipeModel.setThumbsDownCount(mockedDomainRecipe.getThumbsDownCount());
            //Map tags
            recipeModel.setTags(mockedDomainRecipe.getTags());
            //Map set of ingredients.
            recipeModel.setIngredients(mockedDomainRecipe.getIngredients());
            //Map method.
            recipeModel.setMethod(mockedDomainRecipe.getMethod());

        }
        return recipeModel;

    }


    /**
     * Creates a list of Recipe Entity objects from a list of Recipe Model objects
     *
     * @param inputRecipeModelList list recipe model object.
     * @return A list of Recipe Entity object.
     */
    static List<RecipeEntity> createMockRecipeEntityListFromModelList(final List<RecipeModel> inputRecipeModelList) {

        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        for (final RecipeModel currentRecipeModel : inputRecipeModelList) {

            final RecipeEntity recipeEntity = createMockRecipeEntityFromModel(currentRecipeModel);

            recipeEntityList.add(recipeEntity);

        }

        return recipeEntityList;
    }

    /**
     * Creates a Recipe Entity object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Entity object.
     */
    static RecipeEntity createMockRecipeEntityFromModel(final RecipeModel inputRecipeModel) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel
                && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getRecipeSummary().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeModel.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeModel.getName());
            //Map uploader
            recipeEntity.setUploader(inputRecipeModel.getUploader());
            //Map recipe summary
            recipeEntity.setRecipeSummary(inputRecipeModel.getRecipeSummary());
            //Map thumbsUp count
            recipeEntity.setThumbsUpCount(inputRecipeModel.getThumbsUpCount());
            //Map thumbsDown count
            recipeEntity.setThumbsDownCount(inputRecipeModel.getThumbsDownCount());
            //Map tags
            recipeEntity.setTags(inputRecipeModel.getTags());
            //Map set of ingredients.
            recipeEntity.setIngredientsMap(inputRecipeModel.getIngredients());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeModel.getMethod());

        }
        return recipeEntity;

    }

}
