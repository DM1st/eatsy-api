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

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateRecipeTests {

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
     * Check the recipe factory correctly calls for the specified recipe to be created by the persistence service
     * and returns the newly created recipeModel
     */
    @Test
    public void checkCreateRecipe() {

        //Setup
        //Create an input recipe model - this will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Mocking
        //Mock the services that are not being tested through these unit tests
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory
                .createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(inputRecipeModel, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        //Confirm that the eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).persistRecipe(mockedPersistedRecipeEntity);
        //check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }

    /**
     * Check the Recipe Factory gracefully deals with null being
     * passed to the service.
     */
    @Test
    public void checkCreateRecipeWithNull() {

        //Expectation
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(null);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the recipe factory correctly calls for the specified recipe to be created by the persistence service
     * and returns the newly created recipeModel whilst gracefully dealing with the fact this recipe has an empty ingredient list
     */
    @Test
    public void checkCreateRecipeWithEmptyIngredientList() {

        //Setup
        //Create an input recipe model(with an empty ingredients list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setIngredients(new HashMap<>());

        //Mocking
        //Mock the services that are not being tested through these unit tests
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory
                .createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(inputRecipeModel, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        //Confirm that the eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).persistRecipe(mockedPersistedRecipeEntity);
        //check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the recipe factory correctly calls for the specified recipe to be created by the persistence service
     * and returns the newly created recipeModel whilst gracefully dealing with the fact this recipe has an empty method
     */
    @Test
    public void checkCreateRecipeWithEmptyMethod() {

        //Setup
        //Create an input recipe model(with an empty method list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setMethod(new TreeMap<>());

        //Mocking
        //Mock the services that are not being tested through these unit tests
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory
                .createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(inputRecipeModel, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        //Confirm that the eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).persistRecipe(mockedPersistedRecipeEntity);
        //check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty RecipeName field.
     */
    @Test
    public void checkCreateRecipeWithEmptyName() {

        //Setup
        //Create an input recipe model(with an empty name)
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setName("         ");

        //Expectation - cannot create a recipe domain object without providing a recipe name.
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty uploader field.
     */
    @Test
    public void checkCreateRecipeWithEmptyUploader() {

        //Setup
        //Create an input recipe model(with an empty uploader)
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setUploader("         ");

        //Expectation - cannot create a recipe domain object without providing a recipe uploader.
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty recipeSummary field.
     */
    @Test
    public void checkCreateRecipeWithEmptySummary() {

        //Setup
        //Create an input recipe model(with an empty summary)
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setRecipeSummary("         ");

        //Expectation - cannot create a recipe domain object without providing a recipe summary.
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory can create a recipe with only the required fields.
     */
    @Test
    public void checkCreateRecipeWithRequiredFields() {

        //Setup
        //Create an input recipe model(with only the required fields)
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        final RecipeModel inputRecipeModel = new RecipeModel();
        inputRecipeModel.setName(recipeModel.getName());
        inputRecipeModel.setUploader(recipeModel.getUploader());
        inputRecipeModel.setRecipeSummary(recipeModel.getRecipeSummary());

        //Mocking
        //Mock the services that are not being tested through these unit tests
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory.
                createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(inputRecipeModel, eatsyRepositoryHandler, recipeMapperHandler);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);

        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        //Confirm that the eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).persistRecipe(mockedPersistedRecipeEntity);
        //check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }


}
