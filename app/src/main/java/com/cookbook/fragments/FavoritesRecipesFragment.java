package com.cookbook.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cookbook.MainActivity;
import com.cookbook.R;
import com.cookbook.adapters.RecipeListAdapter;
import com.cookbook.helpers.FavoritesHelper;
import com.cookbook.helpers.SearchHelper;
import com.cookbook.pojo.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FavoritesRecipesFragment extends Fragment implements Recipe.RecipeClickListener {

    private static final String LOG_TAG = "CookBook";

    List<Recipe> recipes;
    RecyclerView recyclerView;
    SearchView searchView = null;

    public FavoritesRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Любимые рецепты");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_recipes, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recipes_list_menu, menu);

        try {
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchItem.collapseActionView();
            searchView = SearchHelper.getSearchView(getActivity(), searchItem);
            if (searchView!=null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        List<Recipe> filterData = SearchHelper.filterData(FavoritesRecipesFragment.this.recipes, s);
                        RecipeListAdapter adapter = new RecipeListAdapter(FavoritesRecipesFragment.this.getContext(), filterData, FavoritesRecipesFragment.this);
                        recyclerView.setAdapter(adapter);
                        return true;
                    }
                });
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Не удалось произвести поиск",ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recipes = FavoritesHelper.getInstance(getContext()).getFavoriteRecipes();
        initRecycleView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView!=null) searchView.clearFocus(); // закрываем поиск и прячем клавиатуру
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecipeListAdapter adapter = new RecipeListAdapter(getContext(),recipes,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Recipe recipe) {
        RecipeFragment fragment = RecipeFragment.newInstance(recipe.id);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(fragment, true);
        //Snackbar.make(getActivity().findViewById(R.id.root_layout),String.format("Click on %s",recipe.name), Snackbar.LENGTH_SHORT).show();
    }

}
