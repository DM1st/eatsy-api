package org.eatsy.appservice.persistence;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.recipe.service.EatsyRecipeRepository;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Test class for the persistence context of the application.
 * <p>
 * Test cases will work with H2 in-memory database
 * to eliminate the need for configuring and starting an actual database.
 */
//The annotation will disable full auto-configuration applying only enable configuration relevant to JPA tests.
//tests annotated with @DataJpaTest are transactional and roll back at the end of each test
@DataJpaTest
public class EatsyRecipeRepositoryJPATests {

    // TestEntityManager allows us to use EntityManager in DataJpaTests.
    // EntityManager is used by Spring to interact with the persistence context
    @Autowired
    private TestEntityManager testEntityManager;

    //The Eatsy Repository that extends the JPA interface
    @Autowired
    private EatsyRecipeRepository eatsyRecipeRepository;

    //List of recipe entities for use in the test cases.
    private List<RecipeEntity> recipeEntityList;


    /**
     * Setup method before every test.
     * Ensures there are a number of test recipe entities to be used with the JPA repository operations under test
     */
    @BeforeEach
    public void setup() {

        //Create a list of recipe entity test objects for use in each test.
        recipeEntityList = RecipeEntityDataFactory.generateRecipeEntityList(EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES,
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Ensure each recipe entity has a unique key assigned before being added to the database.
        recipeEntityList.forEach(recipeEntity -> recipeEntity.setKey(UUID.randomUUID().toString()));

    }

    /**
     * Check the save recipeEntity operation
     */
    @Test
    public void checkSaveRecipeEntityOperation() {

        //Create test RecipeEntity
        final RecipeEntity expectedRecipeEntity = recipeEntityList.get(0);

        //Execute the method under test
        final RecipeEntity savedRecipe = eatsyRecipeRepository.save(expectedRecipeEntity);

        //Do the assertions
        Assertions.assertNotNull(savedRecipe);
        Assertions.assertFalse(savedRecipe.getKey().isEmpty());
    }

    /**
     * Check the findAll recipeEntity operation
     */
    @Test
    public void checkFindAllRecipeEntitiesOperation() {

        //Setup
        //Add recipeEntities to the test database
        for (final RecipeEntity currentRecipeEntity : recipeEntityList) {
            //Use test entity manager as we are testing findAll()
            //and can decouple the test from using repository save() method
            testEntityManager.persist(currentRecipeEntity);
        }

        //record size of setup list
        final int sizeOfExpectedRecipeEntityList = recipeEntityList.size();


        //Test
        final List<RecipeEntity> actualRecipeEntityList = eatsyRecipeRepository.findAll();

        //Assertions
        Assertions.assertNotNull(actualRecipeEntityList);
        Assertions.assertEquals(actualRecipeEntityList.size(), sizeOfExpectedRecipeEntityList);

    }

    /**
     * Check the deleteById for recipeEntity operation
     */
    @Test
    public void checkDeleteByIdForRecipeEntityOperation() {

        //Setup

        //Add recipeEntities to the test database
        for (final RecipeEntity currentRecipeEntity : recipeEntityList) {
            //Use test entity manager as we are testing findAll()
            //and can decouple the test from using repository save() method
            testEntityManager.persist(currentRecipeEntity);
        }

        //record size of setup list
        final int sizeOfExpectedRecipeEntityList = recipeEntityList.size();

        //Get the RecipeEntity that is going to be deleted
        final int randomNumberInRecipeEntityList = RecipeEntityDataFactory.generateNumber(sizeOfExpectedRecipeEntityList);
        final int indexOfRecipeEntityToDelete = randomNumberInRecipeEntityList - 1; //Avoid possible index out of bounds exception case
        final RecipeEntity recipeEntityForDeletion = recipeEntityList.get(indexOfRecipeEntityToDelete);


        //Test
        eatsyRecipeRepository.deleteById(recipeEntityList.get(indexOfRecipeEntityToDelete).getKey());
        final Optional<RecipeEntity> recipeEntityOptional = eatsyRecipeRepository.findById(recipeEntityForDeletion.getKey());

        //Assertions
        Assertions.assertFalse(recipeEntityOptional.isPresent());
    }

}

/**
 * @DataJpaTest annotation needs the spring boot configuration found in @SpringBootApplication.
 * It will not find this config as it is declared in the Controller Gradle Project.
 * Therefore, we must add one to this test code in this sub-gradle project.
 */
@SpringBootApplication
class TestApplication {
}