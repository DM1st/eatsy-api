package org.eatsy.appservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/**
 * The domain object for a RecipeImage to hold its information such as name, fileType and the image itself as a byte[].
 * A domain object is enforced to make sure the API controls which fields are required for a
 * RecipeImage on creation and which are not.
 */

//Lombok
@Getter
@ToString
@EqualsAndHashCode
public class RecipeImage {

    //Unique ID
    private final String key;

    //Image name
    private final String imageName;

    //type of file e.g image/jpeg
    private final String imageType;

    //image as byte array
    private final byte[] picByte;

    //Constructor
    private RecipeImage(final RecipeImageBuilder builder) {
        key = builder.key;
        imageName = builder.imageName;
        imageType = builder.imageType;
        picByte = builder.picByte;
    }

    /**
     * Builder for the Recipe Image Object.
     * All fields are mandatory.
     */
    public static class RecipeImageBuilder {
        //required fields
        private String key; //may need to be overwritten (e.g. by mapper)
        private final String imageName;
        private final String imageType;
        private final byte[] picByte;


        //constructor - all fields are mandatory
        public RecipeImageBuilder(final String imageName, final String imageType, final byte[] picByte) {
            this.key = UUID.randomUUID().toString();
            this.imageName = imageName;
            this.imageType = imageType;
            this.picByte = picByte;
        }

        //If the key needs to be overwritten (e.g. by a mapper)
        public RecipeImageBuilder withSpecifiedKey(final String specifiedKey) {
            this.key = specifiedKey;
            return this;
        }

        public RecipeImage build() {
            return new RecipeImage(this);
        }
    }
}
