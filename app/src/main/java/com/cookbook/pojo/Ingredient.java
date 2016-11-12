package com.cookbook.pojo;

import com.cookbook.helpers.StringsUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {
    public long id;
    public String caption;

    @JsonCreator
    public Ingredient(@JsonProperty("id") long id, @JsonProperty("name") String caption) {
        this.id = id;
        this.caption = StringsUtils.capitalize(caption);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ingredient) {
            Ingredient i = (Ingredient)obj;
            return i.caption.equals(this.caption);
        }
        return false;
    }

    public interface IngredientClickListener {
        void onClick(Ingredient i);
    }
}
