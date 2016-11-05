package com.cookbook.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cookbook.ButtonRemoveClickListener;
import com.cookbook.MainActivity;
import com.cookbook.R;
import com.cookbook.adapters.IngAutoCompleteAdapter;
import com.cookbook.adapters.IngredientListAdapter;
import com.cookbook.helpers.DBCategoriesHelper;
import com.cookbook.helpers.DBSearchHelper;
import com.cookbook.pojo.Category;
import com.cookbook.pojo.Ingredient;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class SearchRecipeFragment extends Fragment implements View.OnClickListener, ButtonRemoveClickListener {

    private static final String LOG_TAG = SearchRecipeFragment.class.getName();
    IngredientListAdapter adapter;
    AutoCompleteTextView etIng;
    ExpandableRelativeLayout expandableLayout;
    ImageView imgChevron;
    RadioGroup rg1, rg2;
    TextView tvSelectedIng;
    EditText etDish;
    Spinner spCategories;

    List<Category> categories;

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Найти рецепт");
        setHasOptionsMenu(true);

        categories = new DBCategoriesHelper(getContext()).getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);
        initControls(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.additionalParamsLayout || v.getId() == R.id.ivChevron) {
            toggleAdditionalParams();
        } else if (v.getId() == R.id.btnSearch)
            performSearch(expandableLayout.isExpanded());
    }

    private void toggleAdditionalParams() {
        if (expandableLayout.isExpanded()) {
            expandableLayout.collapse();
            imgChevron.setImageResource(R.drawable.ic_expand_more);
        } else {
            expandableLayout.expand();
            imgChevron.setImageResource(R.drawable.ic_expand_less);
        }
    }

    private void addIngredientToList(Ingredient ing) {
        if (adapter.contains(ing)) {
            new AlertDialog.Builder(getContext())
                    .setMessage(ing + " уже есть в списке!")
                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            adapter.add(ing);
        }
        etIng.setText("");
        tvSelectedIng.setVisibility(View.VISIBLE);

    }

    private void initControls(View view) {

        //Инициализация автодополнения по ингредиентам
        etIng = (AutoCompleteTextView) view.findViewById(R.id.etIngredient);
        etIng.setAdapter(new IngAutoCompleteAdapter(getContext()));
        etIng.setThreshold(3);
        etIng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Ingredient i = (Ingredient) adapterView.getItemAtPosition(position);
                etIng.setText(i.caption);
                addIngredientToList(i);
            }
        });

        etDish = (EditText) view.findViewById(R.id.etDish);

        //Инициализация экспандера с доп. параметрами поиска
        view.findViewById(R.id.additionalParamsLayout).setOnClickListener(this);
        expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
        expandableLayout.collapse();
        imgChevron = (ImageView) view.findViewById(R.id.ivChevron);
        imgChevron.setOnClickListener(this);
        tvSelectedIng = (TextView) view.findViewById(R.id.tvSelectedIng);

        adapter = new IngredientListAdapter(new ArrayList<Ingredient>(), this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        spCategories = (Spinner) view.findViewById(R.id.spinnerCategories);
        ArrayList<String> categoriesCaptions = new ArrayList<>();
        categoriesCaptions.add("Все категории");
        for (Category c : categories) {
            categoriesCaptions.add(c.name);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoriesCaptions);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(spinnerArrayAdapter);

        initRadioButtons(view);
    }

    private void performSearch(boolean withAdditionalParams) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        List<Ingredient> ingredients = adapter.getIngredients();
        String dish = etDish.getText().toString().trim();

        if (dish.isEmpty() && ingredients.isEmpty()) {
            showErrorDialog();
            return;
        }

        DBSearchHelper.Builder builder = getBuilder(withAdditionalParams, dish, ingredients);

        Pair<Boolean, List<Long>> searchResult = builder.search();
        boolean exactlyFound = searchResult.first;
        List<Long> recipesIds = searchResult.second;

        if (recipesIds.size() != 0) {
            if (!exactlyFound)
                Snackbar.make(getView(), "Отображаются рецепты, содержащие указанные ингредиенты", Snackbar.LENGTH_SHORT).show();

            RecipesListFragment recList = RecipesListFragment.newInstance("Результаты поиска", recipesIds);
            MainActivity activity = (MainActivity) getActivity();
            activity.setFragment(recList, true);
        } else if (withAdditionalParams) { // рецепты найти не удалсоь, но поиск производился с учетом доп. параметров. Предлагаем повторить поиск без их учета
            new AlertDialog.Builder(getContext())
                    .setTitle("Таких рецептов не нашлось")
                    .setMessage("Не удалось найти ни одного рецепта, подходящего под заданные критерии поиска.\nПовторить поиск без учета дополнительных параметров?")
                    .setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            expandableLayout.collapse();
                            performSearch(false);
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        } else { // рецепты не найдены. Выводим диалог
            new AlertDialog.Builder(getContext())
                    .setTitle("Таких рецептов не нашлось")
                    .setMessage("Не удалось найти ни одного рецепта с таким названием.\nУкажите другое название блюда, либо заполните ингредиенты")
                    .setPositiveButton("Ок", null)
                    .show();
        }
    }

    @NonNull
    private DBSearchHelper.Builder getBuilder(boolean withAdditionalParams, String caption, List<Ingredient> ingredients) {
        DBSearchHelper.Builder builder = new DBSearchHelper.Builder(getContext());

        if (!ingredients.isEmpty()) {
            builder.withIngredients(ingredients);
        }
        if (!caption.isEmpty()) {
            builder.withCaption(caption);
        }
        if (withAdditionalParams && expandableLayout.isExpanded()) { // учитывать доп. параметры
            int selectedIndex = spCategories.getSelectedItemPosition();
            if (selectedIndex != 0) {
                Category category = categories.get(selectedIndex);
                builder.inCategory(category.id);
            }

            int rId = getCheckedRadioButtonId();
            if (rId == R.id.rbLight)
                builder.withCookingTime(15);
            else if (rId == R.id.rbMedium)
                builder.withCookingTime(30);
            else if (rId == R.id.rbNourishing)
                builder.withCookingTime(45);
        }
        return builder;
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Невозможно начать поиск")
                .setMessage("Введите название блюда, которое хотите найти, либо выбирите ингредиенты, по которым хотите подобрать блюдо")
                .setPositiveButton("Ok", null)
                .show();
    }

    @Override
    public void onClick(int position) {
        adapter.remove(position);
        if (adapter.getItemCount() == 0) {
            tvSelectedIng.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //region Radio buttons
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

    private void initRadioButtons(View view) {
        rg1 = (RadioGroup) view.findViewById(R.id.rg1);
        rg2 = (RadioGroup) view.findViewById(R.id.rg2);
        rg1.clearCheck();
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);
        rg2.check(R.id.rbAny);
    }

    private int getCheckedRadioButtonId() {
        if (rg1.getCheckedRadioButtonId() != -1)
            return rg1.getCheckedRadioButtonId();
        else return rg2.getCheckedRadioButtonId();
    }
    //endregion
}
