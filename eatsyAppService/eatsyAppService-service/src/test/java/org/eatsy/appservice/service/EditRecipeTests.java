package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
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
 * Recipe Factory unit tests for the Edit Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EditRecipeTests {

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
     * Check the recipe factory correctly calls for the specified recipe to be updated by the persistence service
     * and returns the newly updated recipeModel
     */
    @Test
    public void checkEditRecipeNameSuccess() {

        //Setup
        //In order to test editing/updating, recipes must be pre-existing therefore recipes will be returned via the mocks as part of the setup
        final List<RecipeModel> inputRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Add unique key to each recipeModel in the list
        inputRecipeModelList.forEach(currentRecipeModel -> currentRecipeModel.setKey(UUID.randomUUID().toString()));
        //Get a new name to edit the recipe with
        final String newRecipeName = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE).getName();
        //Select a recipe in the list for the recipeName edit at random and get the key
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelList.size()));
        final String uniqueKeyOfRecipeToEdit = inputRecipeModelList.get(randomListIndex).getKey();


        //Expected
        //What we expect the recipeModel to be after the name update (also what we will be submitting to the service as the updated model)
        final RecipeModel expectedUpdatedRecipeModel = inputRecipeModelList.get(randomListIndex);
        expectedUpdatedRecipeModel.setName(newRecipeName);

        //Setup Mocks
        //Mock the services that are not being tested through these unit tests
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory
                .createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(expectedUpdatedRecipeModel, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        //Edit one of the recipe names of the existing recipes.
        final RecipeModel actualUpdatedRecipeModel = recipeFactoryHandler.updateRecipe(uniqueKeyOfRecipeToEdit, expectedUpdatedRecipeModel);

        //Assert
        //Confirm that the eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).persistRecipe(mockedPersistedRecipeEntity);
        //check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(expectedUpdatedRecipeModel, actualUpdatedRecipeModel);

    }

}
