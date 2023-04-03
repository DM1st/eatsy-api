package org.eatsy.appservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The domain object for a Recipe to hold its information such as method, ingredients and name.
 * A domain object is enforced to make sure the API controls which fields are required for a
 * Recipe on creation and which are not.
 */

//Lombok
@Getter
@ToString
@EqualsAndHashCode
public final class Recipe {

    //Unique identifier for recipe object
    private final String key;

    //Recipe name.
    private final String name;

    //Name of the person who added the recipe
    private final String uploader;

    //Short summary/overview of the recipe
    private final String recipeSummary;

    //Number of thumbs up/likes the recipe has received
    private final Integer thumbsUpCount;

    //Number of thumbs down/dislikes the recipe has received
    private final Integer thumbsDownCount;

    //Tags to be associated with the recipe to help with sorting by category
    private final Set<String> tags;

    //The ingredients for the recipe
    private final Map<Integer, String> ingredients;

    //The method for creating the recipe from the ingredients
    private final Map<Integer, String> method;

    //The set of recipeImages for the recipe
    private final Set<RecipeImage> recipeImageSet;

    //Constructor
    private Recipe(final RecipeBuilder builder) {
        key = builder.key;
        name = builder.name;
        uploader = builder.uploader;
        recipeSummary = builder.recipeSummary;
        thumbsUpCount = builder.thumbsUpCount;
        thumbsDownCount = builder.thumbsDownCount;
        ingredients = builder.ingredients;
        method = builder.method;
        tags = builder.tags;
        recipeImageSet = builder.recipeImageSet;
    }

    /**
     * Builder for the Recipe Object.
     */
    /*
    This will help avoid multiple constructors and helps control which fields
    are required for a Recipe on creation and which are not.
     */
    public static class RecipeBuilder {

        //Required
        private final String name;
        private final String uploader;
        private final String recipeSummary;
        private Integer thumbsUpCount;
        private Integer thumbsDownCount;
        private String key;
        private final Set<RecipeImage> recipeImageSet;
        //Optional
        private Map<Integer, String> ingredients;
        private Map<Integer, String> method;
        private Set<String> tags;

        //Constructor for the mandatory Recipe object fields
        public RecipeBuilder(final String name, final String uploader, final String recipeSummary, final Set<RecipeImage> recipeImageSet) {
            this.key = UUID.randomUUID().toString();
            this.thumbsUpCount = 0;
            this.thumbsDownCount = 0;
            this.name = name;
            this.uploader = uploader;
            this.recipeSummary = recipeSummary;
            this.recipeImageSet = recipeImageSet;
        }

        public RecipeBuilder withIngredients(final Map<Integer, String> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder withMethod(final Map<Integer, String> method) {
            this.method = method;
            return this;
        }

        public RecipeBuilder withTags(final Set<String> tags) {
            this.tags = tags;
            return this;
        }

        //If the 0 count (at recipe creations) needs to be overwritten due to an
        // existing model/entity being mapped to a recipe object
        public RecipeBuilder withThumbsUpCount(final Integer thumbsUpCount) {
            this.thumbsUpCount = thumbsUpCount;
            return this;
        }

        //If the 0 count (at recipe creations) needs to be overwritten due to an
        // existing model/entity being mapped to a recipe object
        public RecipeBuilder withThumbsDownCount(final Integer thumbsDownCount) {
            this.thumbsDownCount = thumbsDownCount;
            return this;
        }

        //If the key needs to be overwritten (e.g. by a mapper)
        public RecipeBuilder withSpecifiedKey(final String specifiedKey) {
            this.key = specifiedKey;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }


    }
}
