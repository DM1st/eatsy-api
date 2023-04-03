package org.eatsy.appservice.service;


import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.model.RecipeMediaCardModel;
import org.eatsy.appservice.model.RecipeModel;

import java.util.List;
import java.util.Set;

/**
 * Interface for interacting with recipes
 */
public interface RecipeFactory {

    /**
     * Retrieves all recipe model objects.
     *
     * @return The list of all recipe model objects that exist.
     */
    List<RecipeModel> retrieveAllRecipes();

    /**
     * Creates and persists a new Recipe.
     * These will be persisted via the Recipe Domain to ensure the model recipes are of allowed composition.
     *
     * @param recipeMediaCardModel the recipeMediaCard model that has the data (and media/image content) for the new Recipe
     * @return a recipe model object containing the non-media/image data from the newly created and persisted recipe.
     */
    RecipeModel createRecipe(RecipeMediaCardModel recipeMediaCardModel);

    /**
     * Deletes the requested recipeModel and returns the updated list of recipes
     *
     * @param recipeKey the ID for the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeKey
     */
    List<RecipeModel> deleteRecipeAndReturnUpdatedRecipeList(String recipeKey);

    /**
     * Replaces the existing recipe with the updated version supplied.
     *
     * @param recipeMediaCardModelWithUpdated the recipeMediaCard model with the updated changes to be persisted.
     * @param recipeKey                       the unique ID of the recipe. This will allow the recipe that needs to be
     *                                        updated to be identified.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    RecipeModel updateRecipe(String recipeKey, RecipeMediaCardModel recipeMediaCardModelWithUpdated);

    //TODO
    Set<ImageModel> retrieveCorrespondingImageModels(String recipeKey);
}
