package org.eatsy.appservice.controller.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.service.RecipeFactory;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactoryHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Controller unit tests with Mockito.
 */
//These two annotations tell Mockito to create the mocks based on the @Mock annotation and enable autowired
@SpringBootTest
@AutoConfigureMockMvc
//Define lifecycle of tests to be per class rather than per class. Allows use of @BeforeAll.
//Responses are mocked so PER_METHOD is not needed
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiControllerTest {

    //mockMvc auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    private MockMvc mockMvc;

    //ObjectMapper auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    private ObjectMapper objectMapper;

    //Tells Mockito to mock the RecipeFactory instance
    @MockBean
    private RecipeFactory recipeFactoryHandler;

    //Factory where the data generation methods are stored
    private RecipeModelDataFactory recipeModelDataFactory;

    //Max value for the generated number of ingredients in the recipe
    private int maxIngredientSetSize;

    //Max value for the generated number of method steps in the recipe
    private int maxMethodMapSize;

    @BeforeAll
    public void setup() {
        recipeModelDataFactory = new RecipeModelDataFactoryHandler();
        maxIngredientSetSize = 20;
        maxMethodMapSize = 10;
    }

    /**
     * Test the add recipe endpoint
     */
    @Test
    public void checkAddRecipeSuccess() {

        //Setup - create a recipeModel object for mocking the RecipeFactory service whilst the /add endpoint
        // (in the REST controller) is under test.
        final RecipeModel recipeModel = recipeModelDataFactory.generateRandomRecipeModel(maxIngredientSetSize, maxMethodMapSize);

        //Executes some code of the class under test. In this case, build the mock request that will hit the
        // "/add" endpoint and trigger the below chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.post("/api/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(recipeModel));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When the created recipe is returned it will have a UUID - update this for mock response.
        final RecipeModel recipeModelToReturn = new RecipeModel();
        recipeModelToReturn.setKey(UUID.randomUUID().toString());
        recipeModelToReturn.setName(recipeModel.getName());
        recipeModelToReturn.setIngredientSet(recipeModel.getIngredientSet());
        recipeModelToReturn.setMethod(recipeModel.getMethod());

        //Configure the mock to return the recipeModel when the createRecipeModel is called.
        //This chain method mocks the createRecipe() method call in the RecipeFactory, so every time the method is
        // called within the controller (triggered later in this test), it will return the specified value
        // in the parameter of the thenReturn() method.
        // In this case it returns the pre-set recipeModel (defined in this test setup), instead of actually calling
        // the createRecipe() method in the RecipeFactory service.
        Mockito.when(recipeFactoryHandler.createRecipe(recipeModel)).thenReturn(recipeModelToReturn);

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ingredientSet").exists());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the retrieve all recipes endpoint
     */
    @Test
    public void checkRetrieveAllRecipesSuccess() {

        //Create a list of recipes to return in the mock;
        List<RecipeModel> allRecipes = createRecipeModelList();
        //Configure the mock to return the recipes when the retrieveAllRecipes is called.
        Mockito.when(recipeFactoryHandler.retrieveAllRecipes()).thenReturn(allRecipes);

        //Build the mock request that will hit the "/retrieveAllRecipes" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.get("/api/retrieveAllRecipes")
                    .contentType(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[1].name", is("rice crispies cereal")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the delete recipe endpoint.
     */
    @Test
    public void checkDeleteRecipeEndpointSuccess() {

        //Create a list of recipes to return in the mock;
        List<RecipeModel> allRecipes = createRecipeModelList();
        //Configure the mock to return the recipe model list when the deleteRecipe endpoint is called.
        String key = UUID.randomUUID().toString();
        Mockito.when(recipeFactoryHandler.deleteRecipe(key)).thenReturn(allRecipes);

        //Build the mock request that will hit the "/deleteRecipe" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.delete("/api/deleteRecipe?recipeKey={key}", key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[1].name", is("rice crispies cereal")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the edit recipe endpoint.
     */
    @Test
    public void checkEditRecipeEndpointSuccess() {

        //Setup
        //Create two recipes in the list of recipes
        List<RecipeModel> allRecipes = createRecipeModelList();

        //Create an updated version of one of the two recipes
        RecipeModel updatedRecipe = allRecipes.get(0);
        String updatedRecipeName = "Updated name";
        updatedRecipe.setName(updatedRecipeName);

        //Configure the mock to return the updated recipe when the edit endpoint is called.
        Mockito.when(recipeFactoryHandler.updateRecipe(updatedRecipe.getKey(), updatedRecipe)).thenReturn(updatedRecipe);

        //Build the mock request that will hit the "/edit/{recipeKey}" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        String recipeKey = updatedRecipe.getKey();
        try {
            mockRequest = MockMvcRequestBuilders.put("/api/edit/" + recipeKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(updatedRecipe));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(updatedRecipeName)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Creates recipe model list consisting of two recipes.
     *
     * @return list of all recipe models
     */
    private List<RecipeModel> createRecipeModelList() {

        List<RecipeModel> allTheRecipes = new ArrayList<>();

        //First recipe
        String recipeName = "rice crispies cereal";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("rice crispies");
        ingredientSet.add("Milk");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Put ricpe crispies in bowl");
        method.put(2, "Add milk");
        method.put(3, "listen for the snap crackle and pop");
        final RecipeModel recipeModelOne = new RecipeModel();
        recipeModelOne.setKey(UUID.randomUUID().toString());
        recipeModelOne.setName(recipeName);
        recipeModelOne.setIngredientSet(ingredientSet);
        recipeModelOne.setMethod(method);
        allTheRecipes.add(recipeModelOne);

        //Second recipe
        String recipeNameTwo = "cocopops cereal";
        Set<String> ingredientSetTwo = new HashSet<>();
        ingredientSet.add("cocopops");
        ingredientSet.add("Milk");
        Map<Integer, String> methodTwo = new HashMap<>();
        method.put(1, "Put cocopops in bowl");
        method.put(2, "Add milk");
        method.put(3, "wait for the milk to go chocolatey");
        final RecipeModel recipeModelTwo = new RecipeModel();
        recipeModelTwo.setKey(UUID.randomUUID().toString());
        recipeModelTwo.setName(recipeName);
        recipeModelTwo.setIngredientSet(ingredientSet);
        recipeModelTwo.setMethod(method);
        allTheRecipes.add(recipeModelTwo);

        return allTheRecipes;
    }

}
