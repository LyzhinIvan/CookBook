package com.cookbook.fragments;


import android.os.Bundle;
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
import com.cookbook.helpers.DBRecipesHelper;
import com.cookbook.helpers.SearchHelper;
import com.cookbook.pojo.Recipe;

import java.util.List;


public class RecipesListFragment extends Fragment implements Recipe.RecipeClickListener {
    private static final String ARG_CAT_NAME = "catName";
    private static final String ARG_CATEGORY_ID = "category_id";

    private static final String LOG_TAG = "CookBook";

    List<Recipe> recipes;
    RecyclerView recyclerView;

    SearchView searchView = null;


    public RecipesListFragment() {
        // Required empty public constructor
    }

    public static RecipesListFragment newInstance(long catId, String categoryName) {
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, catId);
        args.putString(ARG_CAT_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long catId = getArguments().getLong(ARG_CATEGORY_ID);
            String name = getArguments().getString(ARG_CAT_NAME);

            DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(getContext()); // получаем все рецепты данной категории из базы
            recipes = dbRecipesHelper.getByCategory(catId);

            getActivity().setTitle(name);
        }
        else {
            Log.e(LOG_TAG,"Создан фрагмент RecipesList без аргументов!");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recipes_list_menu, menu);

        try {
            MenuItem searchItem = menu.findItem(R.id.action_search);
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
                        List<Recipe> filterData = SearchHelper.filterData(RecipesListFragment.this.recipes, s);
                        RecipeListAdapter adapter = new RecipeListAdapter(RecipesListFragment.this.getContext(), filterData, RecipesListFragment.this);
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
        initRecycleView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView!=null) searchView.clearFocus(); // закрываем поиск и прячем клавиатуру
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecipeListAdapter adapter = new RecipeListAdapter(getContext(), recipes, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(Recipe recipe) {
        RecipeFragment fragment = RecipeFragment.newInstance(recipe.id);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFragment(fragment, true);
    }


}
