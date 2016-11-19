package com.cookbook.pojo;

import android.graphics.Bitmap;

import com.cookbook.helpers.BitmapHelper;
import com.cookbook.helpers.StringsUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Recipe {

    public long id;
    public String name;
    public int cookingTime;
    public long categoryId;
    public String instruction;

    @JsonIgnore
    public Bitmap icon;

    @JsonCreator
    public Recipe(@JsonProperty("id") long id,
                  @JsonProperty("name") String name,
                  @JsonProperty("time") int cookingTime,
                  @JsonProperty("category") long categoryId,
                  @JsonProperty("instruction") String instruction,
                  @JsonProperty("icon") byte[] iconBytes) {
        this.id = id;
        this.name = StringsUtils.capitalize(name);
        this.cookingTime = cookingTime;
        this.categoryId = categoryId;
        this.instruction = instruction;
        this.icon = BitmapHelper.getImage(iconBytes);
    }

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }
}
