package org.eatsy.appservice.testdatageneration;


import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.annotations.Generated;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters.*;

/**
 * Class to persist test data through the controller layer into the database.
 * Used to generate test data on demand and persist it for manual test/validation purposes.
 * This is executed via the gradle task "generateRandomRecipes"
 */
@Generated
public class GenerateAndPersistRandomRecipes {

    //logger
    private static final Logger logger = LogManager.getLogger(GenerateAndPersistRandomRecipes.class);

    // Main method acts as the entry point from the gradle task defined in the root gradle project
    public static void main(final String[] args) throws IOException, InterruptedException {
        persistRandomRecipes();
    }

    /**
     * Creates a list of random RecipeModel objects and persists them by using the local deployment of the Eatsy-API.
     * The number of recipes creates is a random number between 1 and 15
     *
     * @throws IOException
     */
    public static void persistRandomRecipes() throws IOException, InterruptedException {

        //Create a list of Recipe Model objects
        final List<RecipeModel> recipeModelList = RecipeModelDataFactory
                .generateRecipeModelsList(MAX_NUMBER_OF_RECIPES, MAX_INGREDIENT_SET_SIZE, MAX_METHOD_MAP_SIZE);

        for (final RecipeModel currentRecipeModel : recipeModelList) {

            // Create a Gson instance to convert the RecipeModel instance to JSON format
            final Gson gson = new Gson();
            // Create a HttpClient instance to send an HTTP POST request to the addRecipe endpoint
            final HttpClient httpClient = HttpClientBuilder.create().build();
            // Create an HttpPost instance to send an HTTP POST request to the addRecipe endpoint
            final HttpPost post = new HttpPost(EatsyRecipeTestParameters.ADD_RECIPE_TO_LOCAL_INSTANCE);
            // Create a MultipartEntityBuilder to build the multipart request body
            final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            // Add the RecipeMediaCardModel JSON  with the appropriate content type
            entityBuilder.addTextBody("recipeModel", gson.toJson(currentRecipeModel), ContentType.APPLICATION_JSON);

            // Create a list to store the FormBodyParts
            final List<FormBodyPart> formBodyParts = new ArrayList<>();


            //Pick a random number of images for the recipe between 1 and 5 inclusive
            final int numberOfFiles = RecipeModelDataFactory.generateNumber(5);
            for (int i = 0; i < numberOfFiles; i++) {

                //Pause required so that the unsplash endpoint generates a new image each time
                TimeUnit.SECONDS.sleep(2);
                final UrlMultipartFile urlMultipartFile = new UrlMultipartFile("https://source.unsplash.com/random/?food");
                final ByteArrayBody byteArrayBody = new ByteArrayBody(urlMultipartFile.getBytes(), ContentType.IMAGE_JPEG, urlMultipartFile.getOriginalFilename());
                final FormBodyPart part = FormBodyPartBuilder.create()
                        .setName("recipeCardImage")
                        .setBody(byteArrayBody)
                        .build();
                formBodyParts.add(part);

            }


            // Add the FormBodyParts to the entityBuilder
            for (final FormBodyPart currentPart : formBodyParts) {
                entityBuilder.addPart(currentPart);
            }

            // Build the multipart request entity
            final HttpEntity multipartEntity = entityBuilder.build();

            // Set the multipart request entity as the content of the HTTP POST request
            post.setEntity(multipartEntity);

            // Send the HTTP POST request to the addRecipe endpoint and receive the HTTP response
            final HttpResponse response = httpClient.execute(post);

            // Read the response
            final String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
        }
    }

}
