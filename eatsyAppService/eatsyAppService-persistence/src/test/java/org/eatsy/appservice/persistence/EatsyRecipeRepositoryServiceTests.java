package org.eatsy.appservice.persistence;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepository;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepositoryHandler;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * EatsyRepository unit tests for the EatsyRepositoryHandler persistence service
 */

//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EatsyRecipeRepositoryServiceTests {

    //Tells Mockito to mock the eatsyRepository instance
    @Mock
    private EatsyRecipeRepository eatsyRecipeRepository;

    /**
     * Class under test
     */
    @InjectMocks
    private EatsyRecipeRepositoryHandler eatsyRepositoryHandler;

    @BeforeEach
    public void setup() {
        //Initialise the class under test and the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Checks the persistRecipe method in the EatsyRepositoryHandler
     * whilst mocking the Eatsy Repository (JPARepository)
     */
    @Test
    public void checkPersistRecipe() throws IOException {

        //Setup

        //1) Create a recipe entity to persist
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //2) Mock the eatsyRepository JPA functionality
        Mockito.when(eatsyRecipeRepository.save(recipeEntity)).thenReturn(recipeEntity);

        //Test
        final RecipeEntity persistedRecipeEntity = eatsyRepositoryHandler.persistRecipe(recipeEntity);

        //Assertions
        Assertions.assertEquals(persistedRecipeEntity, recipeEntity);
    }

    /**
     * Checks the retrieveAllRecipes method in the EatsyRepositoryHandler
     * whilst mocking the Eatsy Repository (JPARepository)
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup

        //1) Create a list of recipe entity objects to persist
        final List<RecipeEntity> mockedRecipeEntityList = RecipeEntityDataFactory.generateRecipeEntityList(EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES,
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //2) Mock the eatsyRepositiry JPA functionality
        Mockito.when(eatsyRecipeRepository.findAll()).thenReturn(mockedRecipeEntityList);

        //Test
        final List<RecipeEntity> actualRecipeEntity = eatsyRepositoryHandler.retrieveAllRecipes();

        //Assertion
        Assertions.assertEquals(actualRecipeEntity, mockedRecipeEntityList);

    }

    /**
     * Checks the deleteById method in the EatsyRepositoryHandler
     * whilst mocking the Eatsy Repository (JPARepository)
     */
    @Test
    public void checkDeleteRecipeById() {

        //Mock creation

        //Create the list of recipeEntities that will be used to mock the eatsyRepository findAll() method used in this setup
        final List<RecipeEntity> mockedRecipeEntityList = RecipeEntityDataFactory.generateRecipeEntityList(EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES,
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Create the list of recipeEntities that will be used to mock the eatsyRepository findAll() method when the first entry has been deleted
        final List<RecipeEntity> mockedRecipeEntityListAfterOneDeletion = new ArrayList<>(mockedRecipeEntityList);
        mockedRecipeEntityListAfterOneDeletion.remove(0);

        //Mock the eatsyRepository JPA functionality
        //1) Mock the void response of the eatsyRepository deleteById method when the first entry in the list is deleted
        Mockito.doNothing().when(eatsyRecipeRepository).deleteById(mockedRecipeEntityList.get(0).getKey());
        //2) Mock the eatsyRepository findAll() method for before calling the deleteById method
        Mockito.when(eatsyRecipeRepository.findAll()).thenReturn(mockedRecipeEntityList);

        //Test
        //Step 1 - Get all recipes before deletion

        final List<RecipeEntity> recipeEntityListBeforeDeletion = eatsyRepositoryHandler.retrieveAllRecipes();

        //Step 2 - Delete the first entry in the list

        eatsyRepositoryHandler.deleteRecipeById(recipeEntityListBeforeDeletion.get(0).getKey());

        //Step 3 - Get all recipes after deletion

        //Mock the eatsyRepository findAll() method for after calling the deleteById method
        Mockito.when(eatsyRecipeRepository.findAll()).thenReturn(mockedRecipeEntityListAfterOneDeletion);
        final List<RecipeEntity> recipeEntityListAfterDeletion = eatsyRepositoryHandler.retrieveAllRecipes();

        //Assertion
        Assertions.assertTrue(recipeEntityListBeforeDeletion.containsAll(recipeEntityListAfterDeletion));
        Assertions.assertFalse(recipeEntityListAfterDeletion.containsAll(recipeEntityListBeforeDeletion));
    }


}
