package com.cookbook.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cookbook.R;
import com.cookbook.pojo.Ingredient;

import java.util.List;


public class RecipeFragment extends Fragment implements Ingredient.IngredientClickListener {

    private boolean isFavorite = false;
    private List<Ingredient> ingredients;

    public RecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recipe_menu, menu);
        if (isFavorite) {
            menu.findItem(R.id.menu_include).setVisible(false);
        }
        else menu.findItem(R.id.menu_exculde).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_include || id == R.id.menu_exculde) {
            isFavorite = !isFavorite;
            getActivity().invalidateOptionsMenu();
        }
        Log.d("Cook","onOptionsItemSelected");
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //ingredients = DummyIngredients.getIngredients(getContext(),3);
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView recycler = (RecyclerView)getView().findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);

        //RecipeIngredientAdapter adapter = new RecipeIngredientAdapter(getContext(), ingredients, this);
        //recycler.setAdapter(adapter);
    }

    @Override
    public void onClick(Ingredient i) {
        Snackbar.make(getActivity().findViewById(R.id.root_layout),String.format("%s добавлен в список покупок",i.caption), Snackbar.LENGTH_SHORT).show();
    }
}
