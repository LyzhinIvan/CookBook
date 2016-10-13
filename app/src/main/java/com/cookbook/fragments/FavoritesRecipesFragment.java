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

import com.cookbook.R;
import com.cookbook.adapters.RecipeListAdapter;
import com.cookbook.dummy.DummyRecipes;
import com.cookbook.helpers.SearchHelper;
import com.cookbook.pojo.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class FavoritesRecipesFragment extends Fragment implements Recipe.RecipeClickListener {

    private static final String ARG_RECIPES = "recipes";
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<List<Recipe>>(){}.getType();
    private static final String LOG_TAG = "CookBook";

    List<Recipe> recipes;
    RecyclerView recyclerView;
    SearchView searchView = null;

    public static FavoritesRecipesFragment newInstance(List<Recipe> recipes) {
        FavoritesRecipesFragment fragment = new FavoritesRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPES, gson.toJson(recipes, type));
        fragment.setArguments(args);
        return fragment;
    }

    public FavoritesRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipes = gson.fromJson(getArguments().getString(ARG_RECIPES),type);
        }
        getActivity().setTitle("Любимые рецепты");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        recipes = DummyRecipes.getRecipes(getContext(),20);
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
        Snackbar.make(getActivity().findViewById(R.id.root_layout),String.format("Click on %s",recipe.name), Snackbar.LENGTH_SHORT).show();
    }

}
