package org.eatsy.appservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Model for the recipe Media Card object which contains all the recipe content and media.
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "Stores and transports recipe media card data")
public class RecipeMediaCardModel {

    //RecipeModel -which contains all the non-media recipe content
    private RecipeModel recipeModel;

    //Set of Multipart files for all corresponding images for this RecipeCard
    private Set<MultipartFile> recipeCardImages;
}
