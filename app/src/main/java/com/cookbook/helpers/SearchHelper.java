package com.cookbook.helpers;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import com.cookbook.adapters.RecipeListAdapter;
import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchHelper {
    public static List<Recipe> filterData(List<Recipe> recipes, String s) {
        List<Recipe> filterData = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).name.toLowerCase().startsWith(s.toLowerCase()))
                filterData.add(recipes.get(i));
        }
        return filterData;
    }

    @Nullable
    public static SearchView getSearchView(Activity activity, MenuItem searchItem) {
        SearchManager searchManager = (SearchManager)activity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconified(true);
            return searchView;
        }
        return null;
    }
}
