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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookbook.ButtonRemoveClickListener;
import com.cookbook.R;
import com.cookbook.adapters.IngAutoCompleteAdapter;
import com.cookbook.adapters.IngredientListAdapter;
import com.cookbook.pojo.Ingredient;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Objects;


public class SearchRecipeFragment extends Fragment implements View.OnClickListener, ButtonRemoveClickListener {

    IngredientListAdapter adapter;
    AutoCompleteTextView etIng;
    ExpandableRelativeLayout expandableLayout;
    ImageView imgChevron;
    RadioGroup rg1, rg2;
    TextView tvSelectedIng;

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Найти рецепт");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initControls();

    }

    private void initControls() {

        //Инициализация автодополнения по ингредиентам
        etIng = (AutoCompleteTextView)getView().findViewById(R.id.etIngredient);
        etIng.setAdapter(new IngAutoCompleteAdapter(getContext()));
        etIng.setThreshold(3);
        etIng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Ingredient i = (Ingredient) adapterView.getItemAtPosition(position);
                etIng.setText(i.caption);
                addIngredientToList();
            }
        });

        //Инициализация экспандера с доп. параметрами поиска
        RelativeLayout additParams = (RelativeLayout)getView().findViewById(R.id.additionalParamsLayout);
        expandableLayout = (ExpandableRelativeLayout)getView().findViewById(R.id.expandableLayout);
        expandableLayout.collapse();
        imgChevron = (ImageView)getView().findViewById(R.id.ivChevron);
        imgChevron.setOnClickListener(this);
        tvSelectedIng = (TextView) getView().findViewById(R.id.tvSelectedIng);

        adapter = new IngredientListAdapter(getContext(),new ArrayList<String>(),this);
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        additParams.setOnClickListener(this);

        rg1 = (RadioGroup) getView().findViewById(R.id.rg1);
        rg2 = (RadioGroup) getView().findViewById(R.id.rg2);
        rg1.clearCheck();
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);
        rg2.check(R.id.rbAny);
    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null);
                rg2.clearCheck();
                rg2.setOnCheckedChangeListener(listener2);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnAdd) {
            addIngredientToList();
        }
        else if (v.getId()== R.id.additionalParamsLayout || v.getId()== R.id.ivChevron) {
            toggleAdditionalParams();
        }

    }

    private void toggleAdditionalParams() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse();
            imgChevron.setImageResource(R.drawable.ic_expand_more);
        }
        else {
            expandableLayout.expand();
            imgChevron.setImageResource(R.drawable.ic_expand_less);
        }
    }

    private void addIngredientToList() {
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
            } else {
                adapter.add(ing);
                etIng.setText("");
                tvSelectedIng.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(int position) {
        adapter.remove(position);
        if (adapter.getItemCount()==0) {
            tvSelectedIng.setVisibility(View.GONE);
        }
    }
}
