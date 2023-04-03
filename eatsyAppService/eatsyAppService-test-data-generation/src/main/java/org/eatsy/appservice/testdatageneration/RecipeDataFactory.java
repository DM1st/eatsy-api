package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.domain.RecipeImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Interface for recipe test data creation utility methods.
 */
public interface RecipeDataFactory {

    /**
     * Uses the Java Faker library to create a Recipe object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe
     * @return a randomly generated Recipe object.
     */
    static Recipe generateRandomRecipe(final int maxIngredientSetSize, final int maxMethodMapSize) throws IOException {

        //Setup
        //Faker object to generate the test data
        final Faker faker = new Faker();
        //Generate recipe name
        final String recipeName = faker.food().dish();
        //Generate uploader name
        final String uploader = faker.name().username();
        //Generate recipe summary
        final String recipeSummary = faker.shakespeare().romeoAndJulietQuote();
        //Generate thumbs up count
        //Generate tags
        final Set<String> tags = generateTagsSet(4);
        //Generate a set of ingredients for the recipe.
        final Map<Integer, String> generatedIngredientMap = generateIngredientMap(maxIngredientSetSize);
        //Generate a map of method steps.
        final Map<Integer, String> generatedMethodMap = generateMethodMap(maxMethodMapSize);
        //Generate a set of RecipeImage objects
        final Set<RecipeImage> generatedRecipeImageSet = generateRecipeImageSet(5);

        //Construct random recipe
        final Recipe recipe = new Recipe.RecipeBuilder(recipeName, uploader, recipeSummary, generatedRecipeImageSet)
                .withTags(tags)
                .withIngredients(generatedIngredientMap)
                .withMethod(generatedMethodMap)
                .build();

        return recipe;

    }

    /**
     * Generates a set of RecipeImage objects
     *
     * @param maxSetSize Max value for the generated number of RecipeImage objects
     * @return a set of RecipeImage objects
     */
    static Set<RecipeImage> generateRecipeImageSet(final int maxSetSize) throws IOException {

        //Create the RecipeImage set and define the number of RecipeImages in the set.
        final Set<RecipeImage> recipeImageSet = new HashSet<>();
        final int numberOfRecipeImages = generateNumber(maxSetSize);

        //Populate the recipe image set with random recipeImage objects
        for (int i = 0; i < numberOfRecipeImages; i++) {
            final RecipeImage generatedRecipeImage = generateRecipeImage();
            recipeImageSet.add(generatedRecipeImage);
        }

        return recipeImageSet;

    }

    /**
     * Generates a recipeImage with random data
     *
     * @return randomly generated recipeImage object
     */
    static RecipeImage generateRecipeImage() throws IOException {

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Generate a random image for the recipe image and convert it to a byte array.
        final byte[] imageAsByteArray = generateRandomImageAsByteArray();

        final RecipeImage generatedRecipeImage = new RecipeImage
                .RecipeImageBuilder(
                faker.food().dish(),
                "image/jpeg",
                imageAsByteArray).build();

        return generatedRecipeImage;
    }


    /**
     * This method uses unsplash.com to generate a random image of food.
     * The method then reads the image data from the input stream of the URL connection,
     * writes it to a ByteArrayOutputStream object in memory using a temporary buffer,
     * and then creates a new byte array containing the same data by calling the toByteArray method
     * on the ByteArrayOutputStream object.
     * The resulting byte[] array can be used to store the image data.
     *
     * @return a byte[] that stores the randomly generated image data.
     * @throws IOException
     */
    static byte[] generateRandomImageAsByteArray() throws IOException {
        // Construct URL object with a URL that returns a random photo of food
        final URL url = new URL("https://source.unsplash.com/random/?food");
        // Open connection to the URL
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // the stream of bytes that can be read from the URL connection
        final InputStream inputStream = connection.getInputStream();

        //Define stream object that can be written to and resized as needed.
        //Need this stream to write the image data to in memory.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Read data from the input stream into a byte array using a while loop which continues until
        // there are no more bytes in the stream (when bytesRead = -1) ensuring that only the actual
        // number of bytes read from the stream are written to the output stream.
        final byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        final byte[] randomImageBytes = outputStream.toByteArray();

        return randomImageBytes;
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

        //Create the ingredient map and define the number of steps in the method.
        final Map<Integer, String> ingredientMap = new TreeMap<>();
        final int numberOfIngredientSteps = generateNumber(maxIngredientMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the method map with random ingredient steps.
        for (int i = 0; i < numberOfIngredientSteps; i++) {
            ingredientMap.put(i, faker.food().ingredient());
        }

        return ingredientMap;
    }

    /**
     * Generates a set of random tags and size.
     *
     * @param maxTagsSetSize Max value for the generated number of tags in the recipe
     * @return a set of tags.
     */
    static Set<String> generateTagsSet(final int maxTagsSetSize) {

        //Create the tag set and define the number of tags in the recipe.
        final Set<String> tagsSet = new HashSet<>();
        final int numberOfTags = generateNumber(maxTagsSetSize);

        //Populate the tags set with random tags.
        //Faker doesn't have food categories so using capital cities as dummy data
        for (int i = 0; i < numberOfTags; i++) {
            tagsSet.add(new Faker().nation().capitalCity());
        }

        return tagsSet;

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
