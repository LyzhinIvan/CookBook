package com.cookbook.pojo;

import android.graphics.Bitmap;

public class Recipe {
    public String name;
    public Bitmap icon;
    public Satiety satiety;
    public int cookingTime;

    public Recipe(String name, Satiety satiety, int cookingTime, Bitmap icon) {
        this.name = name;
        this.cookingTime = cookingTime;
        this.icon = icon;
        this.satiety = satiety;
    }

    public Recipe(String name, Satiety satiety, int cookingTime) {
        this.name = name;
        this.satiety = satiety;
        this.cookingTime = cookingTime;
    }

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }
}
