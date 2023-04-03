package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;

import java.io.IOException;
import java.util.*;

import static org.eatsy.appservice.testdatageneration.RecipeDataFactory.generateRandomImageAsByteArray;

/**
 * Interface for recipe entity test data creation utility methods
 */
public interface RecipeEntityDataFactory {

    /**
     * Uses the Java Faker library to create a RecipeEntity object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe entity
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe entity
     * @return a randomly generated RecipeEntity object.
     */
    static RecipeEntity generateRandomRecipeEntity(final int maxIngredientSetSize, final int maxMethodMapSize) throws IOException {

        //Setup
        final RecipeEntity recipeEntity = new RecipeEntity();
        //Faker object to generate the test data
        final Faker faker = new Faker();
        //Generate recipe name
        final String recipeName = faker.food().dish();
        //Generate recipe uploader
        final String uploader = faker.name().username();
        //Generate recipe summary
        final String recipeSummary = faker.shakespeare().romeoAndJulietQuote();
        //Generate thumbs up count
        final Integer thumbsUpCount = generateNumber(55);
        //Generate thumbs down count
        final Integer thumbsDownCount = generateNumber(55);
        //Generate tags
        final Set<String> tags = generateTagsSet(5);
        //Generate a set of ingredients for the recipe.
        final Map<Integer, String> generatedIngredientsMap = generateIngredientsMap(maxIngredientSetSize);
        //Generate a map of method steps.
        final Map<Integer, String> generatedMethodMap = generateMethodMap(maxMethodMapSize);
        //Generate a set of recipeImage entities.
        final Set<RecipeImageEntity> generatedRecipeImageSet = generateRecipeImageSet(5);//TODO Extract to constant(both)

        recipeEntity.setName(recipeName);
        recipeEntity.setUploader(uploader);
        recipeEntity.setRecipeSummary(recipeSummary);
        recipeEntity.setThumbsUpCount(thumbsUpCount);
        recipeEntity.setThumbsDownCount(thumbsDownCount);
        recipeEntity.setTags(tags);
        recipeEntity.setIngredientsMap(generatedIngredientsMap);
        recipeEntity.setMethodMap(generatedMethodMap);
        recipeEntity.setRecipeImageEntity(generatedRecipeImageSet);
        return recipeEntity;

    }

    /**
     * Generates a set of RecipeImageEntity objects
     *
     * @param maxRecipeImageSetSize Max value for the generated number of recipeImageEntites in the recipe
     * @return a set of RecipeImageEntity objects
     */
    static Set<RecipeImageEntity> generateRecipeImageSet(final int maxRecipeImageSetSize) throws IOException {

        //Create the RecipeImageEntity set and define the number of RecipeImageEntites in the set.
        final Set<RecipeImageEntity> recipeImageEntitySet = new HashSet<>();
        final int numberOfRecipeImages = generateNumber(maxRecipeImageSetSize);

        //Populate the recipeImageEntitySet with random recipeImageEntityObjects
        for (int i = 0; i < maxRecipeImageSetSize; i++) {
            final RecipeImageEntity generatedRecipeImageEntity = generateRecipeImageEntity();
            recipeImageEntitySet.add(generatedRecipeImageEntity);
        }

        return recipeImageEntitySet;
    }

    /**
     * Generates a recipeImageEntity with random data
     *
     * @return randomly generated recipeImageEntity object
     */
    static RecipeImageEntity generateRecipeImageEntity() throws IOException {

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Generate a random image for the recipe image and convert it to a byte array.
        final byte[] imageAsByteArray = generateRandomImageAsByteArray();//TODO best place to store?+continue

        return null;
    }

    /**
     * Uses the Java Faker library to create a list of RecipeEntity objects
     *
     * @param maxNumberOfRecipes   Max value for the generated number of recipeEntities in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe entity
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe entity
     * @return a randomly generated list of recipeEntity objects.
     */
    static List<RecipeEntity> generateRecipeEntityList(final int maxNumberOfRecipes, final int maxIngredientSetSize, final int maxMethodMapSize) throws IOException {

        //Create the recipe list and define the number of recipes in the list.
        final List<RecipeEntity> recipeEntityList = new ArrayList<>();
        final int numberOfRecipesInList = generateNumber(maxNumberOfRecipes);

        //Populate the recipe list with random recipeModels.
        for (int i = 0; i < numberOfRecipesInList; i++) {
            recipeEntityList.add(generateRandomRecipeEntity(maxIngredientSetSize, maxMethodMapSize));
        }

        return recipeEntityList;
    }

    /**
     * Generates a set of random method steps.
     *
     * @param maxMethodMapSize Max value for the generated number of method steps in the recipe
     * @return a set of method steps.
     */
    static Map<Integer, String> generateMethodMap(final int maxMethodMapSize) {

        //Create the method map and define the number of steps in the method.
        final Map<Integer, String> methodMap = new TreeMap<>();
        final int numberOfMethodSteps = generateNumber(maxMethodMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the method map with random method steps.
        for (int i = 0; i < numberOfMethodSteps; i++) {
            //faker does not produce random recipe steps. However, Harry Potter quotes
            //are similar (in terms of short text sentence). So this will serve as representative test data.
            methodMap.put(i, faker.harryPotter().quote());
        }

        return methodMap;
    }

    /**
     * Generates a set of random method steps.
     *
     * @param maxIngredientsMapSize Max value for the generated number of ingredient steps in the recipe
     * @return a map of ingredients.
     */
    static Map<Integer, String> generateIngredientsMap(final int maxIngredientsMapSize) {

        //Create the ingredients map and define the number of ingredients.
        final Map<Integer, String> ingredientMap = new HashMap<>();
        final int numberOfIngredients = generateNumber(maxIngredientsMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the ingredients map with random ingredients.
        for (int i = 0; i < numberOfIngredients; i++) {
            ingredientMap.put(i, faker.food().ingredient());
        }

        return ingredientMap;
    }

    /**
     * Generates a set of random tags and size.
     *
     * @param maxTagSetSize Max value for the generated number of tags in the recipe
     * @return a set of tags.
     */
    static Set<String> generateTagsSet(final int maxTagSetSize) {

        //Create the tag set and define the number of tags in the recipe.
        final Set<String> tagSet = new HashSet<>();
        final int numberOfTags = generateNumber(maxTagSetSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the ingredient set with random ingredients.
        for (int i = 0; i < numberOfTags; i++) {
            //faker does not produce random recipe categories. However, capital cities
            //are similar (in terms of short text ). So this will serve as representative test data.
            tagSet.add(faker.nation().capitalCity());
        }

        return tagSet;

    }

    /**
     * Generates a random int between 1 and the maxValue parameter.
     *
     * @param maxValue the maximum number you want for the recipe field
     * @return the number of items that this recipe field will have.
     */
    static int generateNumber(final int maxValue) {

        final int minValue = 1;
        final Random random = new Random();
        //Ensure min value is 1 (in the instance maxValue is generated as 0)
        final int generatedNumber = random.nextInt(maxValue - minValue + 1) + minValue;

        return generatedNumber;
    }

}
