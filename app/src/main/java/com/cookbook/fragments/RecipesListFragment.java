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
import com.cookbook.dao.DBRecipesHelper;
import com.cookbook.helpers.FavoritesHelper;
import com.cookbook.helpers.SearchHelper;
import com.cookbook.pojo.Recipe;
import com.google.common.primitives.Longs;

import java.util.List;


public class RecipesListFragment extends Fragment implements Recipe.RecipeClickListener {
    private static final String ARG_SCREEN_NAME = "screenName";
    private static final String ARG_RECIPES_ID = "recs_id";
    private static final String ARG_CATEGORY_ID = "cat_id";
    private static final String ARG_SHOW_FAVORITES = "show_favs";

    private static final String LOG_TAG = "CookBook";

    List<Recipe> recipes;
    RecyclerView recyclerView;

    SearchView searchView = null;
    String screenName;
    FavoritesHelper favs;
    boolean showFavs = false;

    public RecipesListFragment() {
        // Required empty public constructor
    }

    /**
     * Создать фрагмент, отображающий рецепты определенной категории
     *
     * @param screenCapture заголовок экрана
     * @param catId         id категории, рецепты которой будут отображены
     */
    public static RecipesListFragment newInstance(String screenCapture, long catId) {
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, catId);
        args.putString(ARG_SCREEN_NAME, screenCapture);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Создать фрагмент, отображающий избранные рецепты
     *
     * @param screenCapture заголовок экрана
     */
    public static RecipesListFragment newInstance(String screenCapture) {
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle args = new Bundle();

        /*FavoritesHelper favHelper = new FavoritesHelper(context);
        long[] recIds = Longs.toArray(favHelper.getFavoriteRecipeIds());*/

        args.putString(ARG_SCREEN_NAME, screenCapture);
        args.putBoolean(ARG_SHOW_FAVORITES, true);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Создать фрагмент, отображающий набор произвольныех рецептов
     *
     * @param screenCapture заголовок экрана
     * @param recIds        id рецептов, которые необходимо отобразить
     */
    public static RecipesListFragment newInstance(String screenCapture, List<Long> recIds) {
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCREEN_NAME, screenCapture);
        args.putLongArray(ARG_RECIPES_ID, Longs.toArray(recIds));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRecipes();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes_list, container, false);
    }

    private void loadRecipes() {
        if (getArguments() != null) {
            screenName = getArguments().getString(ARG_SCREEN_NAME);

            DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(getContext());
            long catId = getArguments().getLong(ARG_CATEGORY_ID, -1);
            showFavs = getArguments().getBoolean(ARG_SHOW_FAVORITES, false);

            if (catId != -1) {
                recipes = dbRecipesHelper.getByCategory(catId);
            } else if (!showFavs)
            {
                List<Long> recIds = Longs.asList(getArguments().getLongArray(ARG_RECIPES_ID));
                recipes = dbRecipesHelper.getById(recIds);
            }

        } else {
            Log.e(LOG_TAG, "Создан фрагмент RecipesList без аргументов!");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recipes_list_menu, menu);

        try {
            MenuItem searchItem = menu.findItem(R.id.action_search);
            searchView = SearchHelper.getSearchView(getActivity(), searchItem);
            if (searchView != null) {
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
            Log.e(LOG_TAG, "Не удалось произвести поиск", ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(screenName);
        favs = new FavoritesHelper(getContext());
        if (showFavs) {
            List<Long> ids = favs.getFavoriteRecipeIds();
            recipes = new DBRecipesHelper(getContext()).getById(ids);
        }
        initRecycleView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView != null) searchView.clearFocus(); // закрываем поиск и прячем клавиатуру
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
