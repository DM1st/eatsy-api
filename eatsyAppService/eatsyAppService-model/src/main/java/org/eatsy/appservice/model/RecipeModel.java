package org.eatsy.appservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * Model for the recipe object
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "Stores and transports recipe data")
public class RecipeModel {

    @Schema(description = "unique identifier.")
    private String key;

    @Schema(description = "Recipe name.")
    private String name;

    @Schema(description = "Name of the person who added the recipe")
    private String uploader;

    @Schema(description = "Short summary/overview of the recipe")
    private String recipeSummary;

    @Schema(description = "Number of thumbs up/likes the recipe has received")
    private String thumbsUpCount;

    @Schema(description = "Number of thumbs down/dislikes the recipe has received")
    private String thumbsDownCount;

    @Schema(description = "tags to be associated with the recipe to help with sorting by category")
    private Set<String> tags;

    @Schema(description = "The ingredients for the recipe.")
    private Map<String, String> ingredients;

    @Schema(description = "The method for creating the recipe from the ingredients.")
    private Map<String, String> method;


}
