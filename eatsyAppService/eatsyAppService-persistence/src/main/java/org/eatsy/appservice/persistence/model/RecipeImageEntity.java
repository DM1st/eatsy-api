package org.eatsy.appservice.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class to map the recipeImage object with a corresponding table in the database.
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
//persistence
@Entity
@Table(name = "recipe_image")
public class RecipeImageEntity {

    //Primary key
    @Id
    @Column(name = "key")
    private String key;

    //Image name.
    @Column(name = "imageName")
    private String imageName;

    //type of file e.g image/jpeg
    @Column(name = "imageType")
    private String imageType;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "picByte", length = 50000000)
    private byte[] picByte;


}
