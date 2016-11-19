package com.cookbook.helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesHelper extends PreferencesWorker {

    private static final String PREF_FAVORITES = "favorites";

    public FavoritesHelper(Context context) {
        super(context);
    }

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
        prefs.edit().putString(PREF_FAVORITES,"").apply();
    }


    public boolean isFavorite(long id) {
        return getFavoriteRecipeIds().contains(id);
    }

    private void saveLongList(List<Long> savedIds) {
        StringBuilder sb = new StringBuilder();
        for (long sId : savedIds) {
            sb.append(sId).append(" ");
        }
        prefs.edit().putString(PREF_FAVORITES,sb.toString()).apply();
    }

    public List<Long> getFavoriteRecipeIds() {
        String[] savedIds = prefs.getString(PREF_FAVORITES, "").split(" ");
        ArrayList<Long> res = new ArrayList<>();
        for (String id : savedIds) {
            if (!Objects.equals(id, "")) {
                res.add(Long.parseLong(id));
            }
        }
        return res;
    }

}
