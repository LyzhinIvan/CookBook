package com.cookbook.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {
    public long id;
    public String caption;

    @JsonCreator
    public Ingredient(@JsonProperty("id") long id, @JsonProperty("name") String caption) {
        this.id = id;
        this.caption = caption;
    }

    public interface IngredientClickListener {
        void onClick(Ingredient i);
    }
}
