package com.cookbook.rest;

import com.cookbook.pojo.Category;
import com.cookbook.pojo.Ingredient;
import com.cookbook.pojo.Recipe;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UpdateResponse {
    @JsonProperty("recipes")
    public List<Recipe> recipes = new ArrayList<>();
    @JsonProperty("ingredients")
    public List<Ingredient> ingredients = new ArrayList<>();
    @JsonProperty("newUpdated")
    public Long newUpdated;
    @JsonProperty("categories")
    public List<Category> categories = new ArrayList<>();
}
