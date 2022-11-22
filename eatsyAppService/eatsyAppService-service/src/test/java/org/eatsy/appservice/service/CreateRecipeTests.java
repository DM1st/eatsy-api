package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
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

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateRecipeTests {

    //Create a mock implementation of the RecipeMapper. These unit tests are only concerned with the service not the mapper module.
    @Mock
    private RecipeMapper recipeMapperHandler;

    //Create a mock implementation of the EatsyRepositoryService. These unit tests are only concerned with the service not the persistence module.
    @Mock
    private EatsyRepositoryService eatsyRepositoryHandler;

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
     * The RecipeModel being returned from the recipeFactory is formed from
     * the newly created recipe domain object.
     *
     * This test checks the RecipeModel response has the same content as the
     * recipeModel initially requested to the Eatsy App service.
     */
    @Test
    public void checkCreateRecipe() {

        //Setup
        //Create an input recipe model - this will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Mock the services that are not being tested through these unit tests
        createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModel);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert - check the returned model matches the request model used to create the recipe domain object.
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
     * Check the Recipe Factory can create a recipe with an empty Ingredient List
     */
    @Test
    public void checkCreateRecipeWithEmptyIngredientList() {

        //Setup
        //Create an input recipe model(with an empty ingredients list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setIngredientSet(new HashSet<>());
        //Mock the services that are not being tested through these unit tests
        createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModel);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory can create a recipe with an empty method
     */
    @Test
    public void checkCreateRecipeWithEmptyMethod() {

        //Setup
        //Create an input recipe model(with an empty method list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setMethod(new TreeMap<>());
        //Mock the services that are not being tested through these unit tests
        createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModel);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty RecipeName field.
     */
    @Test
    public void checkCreateRecipeWithEmptyName() {

        //Setup
        //Create an input recipe model(with an empty method list)
        //This will also be the expected output from the method under test.
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
        //Mock the services that are not being tested through these unit tests
        createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModel);

        //Test
        final RecipeModel actualRecipeModel = recipeFactoryHandler.createRecipe(inputRecipeModel);

        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services.
     *
     * @param inputRecipeModel randomly generated recipe model test data.
     */
    private void createMocksForRecipeMapperAndEatsyRepositoryServices(final RecipeModel inputRecipeModel) {

        //Configure the eatsyRepository Mock to return the mocked data (Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = createMockRecipeEntity(inputRecipeModel);
        Mockito.when(eatsyRepositoryHandler.persistNewRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapper mock to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = createMockDomainRecipe(inputRecipeModel);
        Mockito.when(recipeMapperHandler.mapToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapToModel(mockedDomainRecipe)).thenReturn(inputRecipeModel);
    }

    /**
     * Creates a Recipe Entity object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Entity object.
     */
    private static RecipeEntity createMockRecipeEntity(final RecipeModel inputRecipeModel) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeModel.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeModel.getName());
            //Map set of ingredients.
            recipeEntity.setIngredientSet(inputRecipeModel.getIngredientSet());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeModel.getMethod());

        }
        return recipeEntity;

    }

    /**
     * Creates a Recipe Domain object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Domain object.
     */
    private static Recipe createMockDomainRecipe(final RecipeModel inputRecipeModel) {

        Recipe domainRecipe = null;
        //The recipe model to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(inputRecipeModel.getName())
                    .withIngredientSet(inputRecipeModel.getIngredientSet())
                    .withMethod(inputRecipeModel.getMethod())
                    .build();

        }
        return domainRecipe;

    }

}
