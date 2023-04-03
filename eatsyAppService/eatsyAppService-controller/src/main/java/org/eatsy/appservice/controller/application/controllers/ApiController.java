package org.eatsy.appservice.controller.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.controller.application.constants.EatsyRecipeEndpoints;
import org.eatsy.appservice.image.data.service.ImageDataFactory;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.model.RecipeMediaCardModel;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.service.RecipeFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * API Controller for creating, editing, deleting and retrieving recipes.
 * All handling methods on this controller are relative to the /api path.
 */
@RestController //Inform the DispatcherServlet that this class contains request mapping methods.
@RequestMapping(EatsyRecipeEndpoints.API)
@EnableAutoConfiguration
public class ApiController {

    //logger
    private static final Logger logger = LogManager.getLogger(ApiController.class);

    //Interface for recipe operations
    private final RecipeFactory recipeFactoryHandler;

    //Interface for recipeImage operations
    private final ImageDataFactory imageDataFactoryHandler;

    //Inject the dependency of the recipeFactory implementation into the api controller during instantiation.
    public ApiController(final RecipeFactory recipeFactoryHandler, final ImageDataFactory imageDataFactoryHandler) {
        this.recipeFactoryHandler = recipeFactoryHandler;
        this.imageDataFactoryHandler = imageDataFactoryHandler;
    }


    /**
     * "Returns a new recipe with the non-media/image information provided in the request"
     *
     * @return the recipe model object that has been created (excludes media/image content).
     */
    @Operation(description = "Returns a new recipe with the (non-media/image) information provided in the request")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully created new recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.ADD_RECIPE, method = {RequestMethod.POST},
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<RecipeModel> addRecipe(
            @RequestPart("recipeModel") final RecipeModel recipeModel, @RequestPart("recipeCardImage") final MultipartFile[] recipeCardImages) {

        logger.debug("A new request has been made to create a recipe called " + recipeModel.getName());
        //TODO tidy up and fix javadoc
        final RecipeMediaCardModel recipeMediaCardModel = new RecipeMediaCardModel();
        recipeMediaCardModel.setRecipeModel(recipeModel);
        recipeMediaCardModel.setRecipeCardImages(new HashSet<>(Arrays.asList(recipeCardImages)));
        final RecipeModel newRecipeModel = recipeFactoryHandler.createRecipe(recipeMediaCardModel);

        final ResponseEntity<RecipeModel> response = new ResponseEntity<RecipeModel>(newRecipeModel, HttpStatus.OK);
        return response;
    }

    /**
     * Retrieves all recipe model objects.
     *
     * @return The model object that has been created detailing all recipes.
     */
    @Operation(description = "Returns all recipes and their associated unique ids that have been created.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully returned all recipes.")})
    @RequestMapping(value = EatsyRecipeEndpoints.RETRIEVE_ALL_RECIPES, method = {RequestMethod.GET})
    @ResponseBody
    public List<RecipeModel> retrieveAllRecipes() {

        logger.debug("A new request has been made to retrieve all recipes");
        final List<RecipeModel> recipeModelList = recipeFactoryHandler.retrieveAllRecipes();
        return recipeModelList;
    }

    /**
     * Deletes the requested recipe
     *
     * @param recipeKey the unique ID of the recipe object requested for deletion.
     * @return the updated list of all model recipe objects that has had the requested recipe removed.
     */
    @Operation(description = "Deletes the submitted recipe and returns the updated list of all recipes")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully deleted chosen recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.DELETE_RECIPE, method = {RequestMethod.DELETE})
    @ResponseBody
    public List<RecipeModel> deleteRecipe(final String recipeKey) {

        logger.debug("A new request has been made to delete recipe: " + recipeKey);
        final List<RecipeModel> updatedRecipeList = recipeFactoryHandler.deleteRecipeAndReturnUpdatedRecipeList(recipeKey);
        return updatedRecipeList;

    }

    /**
     * Replaces the existing recipe with the updated version supplied in the PUT request.
     *
     * @param recipeMediaCardModelWithUpdates the recipeMediaCard model with the updated changes to be persisted.
     * @param recipeKey                       the unique ID of the recipe. This will allow the recipe that needs to be
     *                                        updated to be identified.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    @Operation(description = "Replaces the existing recipe with the updated version supplied in the PUT request")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully updated chosen recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.EDIT_RECIPE, method = {RequestMethod.PUT}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public RecipeModel editRecipe(
            @Parameter(description = "The recipeMediaCardModel with the new information to update the existing recipe")
            @ModelAttribute final RecipeMediaCardModel recipeMediaCardModelWithUpdates, @PathVariable final String recipeKey) {

        logger.debug("A new request has been made to update recipe: " + recipeKey);
        final RecipeModel updatedRecipeModel = recipeFactoryHandler.updateRecipe(recipeKey, recipeMediaCardModelWithUpdates);
        return updatedRecipeModel;
    }

    @GetMapping(path = {"/get/imageModels/{recipeKey}"})
    @ResponseBody
    public Set<ImageModel> getImageModels(@PathVariable("recipeKey") final String recipeKey) {
        final Set<ImageModel> imageModels = recipeFactoryHandler.retrieveCorrespondingImageModels(recipeKey);
        return imageModels;
    }

    //TODO update Docs and logs
    @GetMapping(path = {"/get/image/{imageKey}"}, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageByImageKey(@PathVariable("imageKey") final String imageKey) throws IOException {
        final byte[] image = imageDataFactoryHandler.retrieveImage(imageKey);
        return image;
    }


}
