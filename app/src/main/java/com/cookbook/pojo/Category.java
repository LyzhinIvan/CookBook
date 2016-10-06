package com.cookbook.pojo;

import android.graphics.Bitmap;

import java.util.List;

public class Category {
    public String name;
    public Bitmap icon;
    public List<Recipe> recipes;

    public Category(String name, List<Recipe> recipes, Bitmap icon) {
        this.name = name;
        this.recipes = recipes;
        this.icon = icon;
    }

    public Category(String name, List<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public interface CategoryClickListener {
        void onClick(Category category);
    }
}
