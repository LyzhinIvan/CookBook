package com.cookbook.pojo;

import android.graphics.Bitmap;

import java.util.List;

public class Recipe {
    public String name;
    public Bitmap icon;
    public Satiety satiety;
    public int cookingTime;
    public List<Ingredient> ingredients;

    public Recipe(String name, Satiety satiety, int cookingTime, Bitmap icon) {
        this.name = name;
        this.cookingTime = cookingTime;
        this.icon = icon;
        this.satiety = satiety;
    }

    public Recipe(String name, Satiety satiety, int cookingTime, List<Ingredient> ingredients) {
        this.name = name;
        this.satiety = satiety;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
    }

    public interface RecipeClickListener {
        void onClick(Recipe recipe);
    }
}
