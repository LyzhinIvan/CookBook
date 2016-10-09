package com.cookbook.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cookbook.ButtonRemoveClickListener;
import com.cookbook.R;
import com.cookbook.adapters.IngredientListAdapter;

import java.util.ArrayList;
import java.util.Objects;


public class ShopingListFragment extends Fragment implements View.OnClickListener, ButtonRemoveClickListener {

    RecyclerView recyclerView;
    Button btnAdd;
    EditText etIng;

    IngredientListAdapter adapter;

    public ShopingListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Список покупок");
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView);
        btnAdd = (Button)getView().findViewById(R.id.btnAdd);
        etIng = (EditText)getView().findViewById(R.id.etIngredient);

        adapter = new IngredientListAdapter(getContext(),new ArrayList<String>(), this);
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_recipe_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shoping_list, container, false);
    }

    @Override
    public void onClick(View v) {
        String ing = etIng.getText().toString();
        if (!Objects.equals(ing, "")) {
            if (adapter.contains(ing)) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Такой ингредиент уже есть в списке!")
                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
            else
                adapter.add(ing);
        }
    }

    @Override
    public void onClick(int position) {
        adapter.remove(position);
    }
}
