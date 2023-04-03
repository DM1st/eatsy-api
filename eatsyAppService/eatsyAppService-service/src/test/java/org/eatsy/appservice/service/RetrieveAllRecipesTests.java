package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

/**
 * Recipe Factory unit tests for the Retrieve all Recipes Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RetrieveAllRecipesTests {

    //Create a mock implementation of the RecipeMapper. These unit tests are only concerned with the service module not the mapper module.
    @Mock
    private RecipeMapper recipeMapperHandler;

    //Create a mock implementation of the EatsyRepositoryService. These unit tests are only concerned with the service module not the persistence module.
    @Mock
    private EatsyRecipeRepositoryService eatsyRepositoryHandler;

    /**
     * Class under test.
     */
    //Injects the dependent mocks (marked with @Mock) for a recipe factory instance.
    @InjectMocks
    private RecipeFactoryHandler recipeFactoryHandler;

    @BeforeEach
    public void setup() {
        //Initialise the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
        //Initialise the class under test and inject the mocks.
        recipeFactoryHandler = new RecipeFactoryHandler(recipeMapperHandler, eatsyRepositoryHandler);
    }

    /**
     * Check the recipe factory correctly calls for all recipes to be returned by the persistence service
     * and returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup
        //In order to test recipe retrieval, recipes must be pre-existing therefore recipes will be returned via the mocks as part of the setup
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Add unique key to each recipeModel in the list
        expectedRecipeModelList.forEach(currentRecipeModel -> currentRecipeModel.setKey(UUID.randomUUID().toString()));

        //Mocking
        //Mock the services that are not being tested through these unit tests
        RecipeMockFactory.createMocksForMapperAndPersistenceServicesInRetrieveAllRecipeServiceMethod(
                expectedRecipeModelList, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        final List<RecipeModel> actualRecipeModelsList = recipeFactoryHandler.retrieveAllRecipes();

        //Assert
        //Confirm that the eatsyRepositoryHandler..retrieveAllRecipes() gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).retrieveAllRecipes();
        //check the returned recipeModel list matches the expected recipeModel list.
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

}
