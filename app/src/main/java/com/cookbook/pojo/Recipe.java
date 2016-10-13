package com.cookbook.pojo;

import android.graphics.Bitmap;

import com.cookbook.helpers.BitmapHelper;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "categoryId",
        "name",
        "time",
        "instruction",
        "satiety",
        "picture"
})
public class Recipe {

    public long id;
    public String name;
    public int cookingTime;
    public Satiety satiety;
    public long categoryId;
    public String instruction;

    @JsonIgnore
    public Bitmap icon;

    @JsonCreator
    public Recipe(@JsonProperty("id") long id, @JsonProperty("name") String name,
                  @JsonProperty("time") int cookingTime, @JsonProperty("satiety") Satiety satiety,
                  @JsonProperty("categoryId") long categoryId, @JsonProperty("instruction") String instruction,
                  @JsonProperty("picture") byte[] iconBytes) {
        this.id = id;
        this.name = name;
        this.cookingTime = cookingTime;
        this.satiety = satiety;
        this.categoryId = categoryId;
        this.instruction = instruction;

        this.icon = BitmapHelper.getImage(iconBytes);
    }

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }
}
