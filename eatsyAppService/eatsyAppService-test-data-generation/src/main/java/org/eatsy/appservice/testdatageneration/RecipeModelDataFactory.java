package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.model.RecipeModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interface for recipe model test data creation utility methods
 */
public interface RecipeModelDataFactory {

    /**
     * Uses the Java Faker library to create a RecipeModel object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe model
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe model
     * @return a randomly generated RecipeModel object.
     */
    static RecipeModel generateRandomRecipeModel(final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Setup
        final RecipeModel recipeModel = new RecipeModel();
        //Faker object to generate the test data
        final Faker faker = new Faker();
        //Generate recipe name
        final String recipeName = faker.food().dish();
        //Generate uploader name
        final String uploader = faker.name().username();
        //Generate recipe summary
        final String recipeSummary = faker.shakespeare().romeoAndJulietQuote();
        //Generate thumbs up count
        final Integer thumbsUpCount = generateNumber(55);
        //Generate thumbs down count
        final Integer thumbsDownCount = generateNumber(55);
        //Generate a set of tags
        final Set<String> generatedTagSet = generateTagsSet(5);
        //Generate a map of ingredients for the recipe.
        final Map<Integer, String> generatedIngredientMap = generateIngredientMap(maxIngredientSetSize);
        //Generate a map of method steps.
        final Map<Integer, String> generatedMethodMap = generateMethodMap(maxMethodMapSize);

        final Map<String, String> generatedStringIngredientMap = generatedIngredientMap
                .entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));

        final Map<String, String> generatedStringMethod = generatedMethodMap
                .entrySet().stream().collect(Collectors.toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));

        recipeModel.setName(recipeName);
        recipeModel.setUploader(uploader);
        recipeModel.setRecipeSummary(recipeSummary);
        recipeModel.setThumbsUpCount(thumbsUpCount.toString());
        recipeModel.setThumbsDownCount(thumbsDownCount.toString());
        recipeModel.setTags(generatedTagSet);
        recipeModel.setIngredients(generatedStringIngredientMap);
        recipeModel.setMethod(generatedStringMethod);
        return recipeModel;

    }

    /**
     * Uses the Java Faker library to create a list of RecipeModel objects
     *
     * @param maxNumberOfRecipes   Max value for the generated number of recipeModels in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe model
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe model
     * @return a randomly generated list of recipeModel objects.
     */
    static List<RecipeModel> generateRecipeModelsList(final int maxNumberOfRecipes, final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Create the recipe list and define the number of recipes in the list.
        final List<RecipeModel> recipeModelsList = new ArrayList<>();
        final int numberOfRecipesInList = generateNumber(maxNumberOfRecipes);

        //Populate the recipe list with random recipeModels.
        for (int i = 0; i < numberOfRecipesInList; i++) {
            recipeModelsList.add(generateRandomRecipeModel(maxIngredientSetSize, maxMethodMapSize));
        }

        return recipeModelsList;
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
     * Generates a set of random ingredient steps.
     *
     * @param maxIngredientMapSize Max value for the generated number of ingredient steps in the recipe
     * @return a set of ingredient steps.
     */
    static Map<Integer, String> generateIngredientMap(final int maxIngredientMapSize) {

        //Create the ingredient map and define the number of ingredients.
        final Map<Integer, String> IngredientMap = new HashMap<>();
        final int numberOfIngredientSteps = generateNumber(maxIngredientMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the ingredient map with random ingredients.
        for (int i = 0; i < numberOfIngredientSteps; i++) {
            IngredientMap.put(i, faker.food().ingredient());
        }

        return IngredientMap;
    }

    /**
     * Generates a set of random tags and size.
     *
     * @param maxTagsSetSize Max value for the generated number of tags in the recipe
     * @return a set of tags.
     */
    static Set<String> generateTagsSet(final int maxTagsSetSize) {

        //Create the tags set and define the number of tags in the recipe.
        final Set<String> tagSet = new HashSet<>();
        final int numberOfTags = generateNumber(maxTagsSetSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the ingredient set with random ingredients.
        for (int i = 0; i < numberOfTags; i++) {
            //faker does not produce random recipe categories. However, capital cities
            //are similar (in terms of short text sentence). So this will serve as representative test data.
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
