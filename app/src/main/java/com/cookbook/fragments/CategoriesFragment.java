package com.cookbook.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cookbook.MainActivity;
import com.cookbook.R;
import com.cookbook.adapters.CategoriesGridAdapter;
import com.cookbook.dummy.DummyCategories;
import com.cookbook.pojo.Category;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment implements Category.CategoryClickListener {

    RecyclerView recyclerView;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Категории");
        initRecycleView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.categories_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    private void initRecycleView() {
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        ArrayList<Category> categories = DummyCategories.getCategories(getContext(), 10, 15);
        CategoriesGridAdapter adapter = new CategoriesGridAdapter(getContext(),categories,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Category category) {

    }



}
