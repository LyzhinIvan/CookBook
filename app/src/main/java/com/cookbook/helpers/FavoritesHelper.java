package com.cookbook.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesHelper {

    private static final String APP_PREFS = "CookBook_Prefs", PREF_FAVORITES = "favorites";
    private final SharedPreferences mPrefs;

    //region Singleton
    private static FavoritesHelper instance = null;

    public static FavoritesHelper getInstance(Context context) {
        if (instance == null)
            instance = new FavoritesHelper(context);
        return instance;
    }

    private FavoritesHelper(Context context) {
        mPrefs = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }
    //endregion


    public void addToFavorite(long id) {
        List<Long> savedIds = getFavoriteRecipeIds();
        if (savedIds.contains(id)) return;

        savedIds.add(id);
        saveLongList(savedIds);
    }

    public void removeFromFavorites(long id) {
        List<Long> savedIds = getFavoriteRecipeIds();
        if (!savedIds.contains(id)) return;

        savedIds.remove(id);
        saveLongList(savedIds);
    }

    public void removeAll() {
        mPrefs.edit().putString(PREF_FAVORITES,"").apply();
    }

    /*
    public List<Recipe> getFavoriteRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (Long id : getFavoriteRecipeIds()) {
            recipes.add(dbRecipesHelper.getById(id));
        }
        return recipes;
    }*/

    public boolean isFavorite(long id) {
        return getFavoriteRecipeIds().contains(id);
    }

    private void saveLongList(List<Long> savedIds) {
        StringBuilder sb = new StringBuilder();
        for (long sId : savedIds) {
            sb.append(sId).append(" ");
        }
        mPrefs.edit().putString(PREF_FAVORITES,sb.toString()).apply();
    }

    public List<Long> getFavoriteRecipeIds() {
        String[] savedIds = mPrefs.getString(PREF_FAVORITES, "").split(" ");
        ArrayList<Long> res = new ArrayList<>();
        for (String id : savedIds) {
            if (!Objects.equals(id, "")) {
                res.add(Long.parseLong(id));
            }
        }
        return res;
    }

}
