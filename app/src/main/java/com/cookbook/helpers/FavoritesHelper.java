package com.cookbook.helpers;

import android.content.Context;

import com.cookbook.pojo.Recipe;

import java.util.List;

public class FavoritesHelper {
    //region Singleton
    private static FavoritesHelper instance = null;
    public static FavoritesHelper getInstance(Context context) {
        if (instance==null)
            instance = new FavoritesHelper(context);
        return instance;
    }
    private Context context;
    private FavoritesHelper(Context context) {
        this.context = context;
    }
    //endregion

    public void addToFavorite() {

    }

    public void removeFromFavorites() {

    }

    public List<Recipe> getFavoriteRecipes() {
        return null;
    }
}
