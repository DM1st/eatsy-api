package org.eatsy.appservice.persistence.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Entity class to map the recipe object with a corresponding table in the database.
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
//persistence
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    //Primary key
    @Id
    @Column(name = "key")
    private String key;

    //Recipe name.
    @Column(name = "name")
    private String name;

    //The person who uploaded the recipe
    @Column(name = "uploader")
    private String uploader;

    //The short description of the recipe
    @Column(name = "recipeSummary")
    private String recipeSummary;

    //The number of thumbs up/likes for the recipe
    @Column(name = "thumbsUpCount")
    private Integer thumbsUpCount;

    //The number of thumbs down/dislikes for the recipe
    @Column(name = "thumbsDownCount")
    private Integer thumbsDownCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_key") // Foreign key column in the recipe_image table
    private Set<RecipeImageEntity> recipeImageEntity; //TODO rename this as a set

    //Defines a collection of instances.
    @ElementCollection
    //Join column used to map Recipe entity id (primary key value) to the tags' collection table's ID column.
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "key"))
    @Column(name = "tags")
    private Set<String> tags = new HashSet<>();

    @ElementCollection
    //The map's key is this column for our join table
    @MapKeyColumn(name = "ingredient_step_number")
    //The Map's values corresponds to this column of the join table.
    @Column(name = "ingredient_step")
    //Join column used to map Recipe entity id (primary key value) to the Ingredients' collection table's ID column.
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "key"))
    private Map<Integer, String> ingredientsMap = new HashMap<>();

    @ElementCollection
    //The map's key is this column for our join table
    @MapKeyColumn(name = "method_step_number")
    //The Map's values corresponds to this column of the join table.
    @Column(name = "method_step")
    //Join column used to map Recipe entity id (primary key value) to the method's collection table's ID column.
    @CollectionTable(name = "recipe_method", joinColumns = @JoinColumn(name = "key"))
    private Map<Integer, String> methodMap = new HashMap<>();

}
