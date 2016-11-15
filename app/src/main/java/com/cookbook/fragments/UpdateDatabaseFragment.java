package com.cookbook.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cookbook.R;
import com.cookbook.dao.DBCategoriesHelper;
import com.cookbook.dao.DBIngredientsHelper;
import com.cookbook.dao.DBRecipesHelper;
import com.cookbook.helpers.UpdatingHelper;
import com.cookbook.pojo.Category;
import com.cookbook.pojo.Recipe;
import com.cookbook.rest.DeltaResponse;
import com.cookbook.rest.IRestApi;
import com.cookbook.rest.RestClient;
import com.cookbook.rest.UpdateResponse;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateDatabaseFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = UpdateDatabaseFragment.class.getSimpleName();

    public UpdateDatabaseFragment() {
        // Required empty public constructor
    }

    Button btnUpdate;
    TextView tvUpdatingStatus, tvLastUpdate, tvRecipesInBase;
    View layoutMain, layoutUpdating;
    UpdatingHelper updatingHelper;
    SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");

    IRestApi client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatingHelper = new UpdatingHelper(getContext());
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_db, container, false);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        tvUpdatingStatus = (TextView) view.findViewById(R.id.tvUpdateStatus);
        tvRecipesInBase = (TextView) view.findViewById(R.id.tvRecipesInBase);
        tvLastUpdate = (TextView) view.findViewById(R.id.tvLastUpdateTime);
        layoutMain = view.findViewById(R.id.layoutInfo);
        layoutUpdating = view.findViewById(R.id.layoutUpdateProgress);

        long lastUpdatingTime = updatingHelper.getLastUpdatingTime();
        if (lastUpdatingTime == 0) {
            tvLastUpdate.setText("не производилось");
        } else {
            tvLastUpdate.setText(df.format(lastUpdatingTime));
        }

        btnUpdate.setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Обновление данных");
        client = RestClient.getClient(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.empty_menu, menu);
    }


    @Override
    public void onClick(View v) {
        tvUpdatingStatus.setText("Запрос обновлений...");
        swithVisibility(true);

        client.getDelta(updatingHelper.getLastUpdatingTime()).enqueue(new Callback<DeltaResponse>() {
            @Override
            public void onResponse(Call<DeltaResponse> call, Response<DeltaResponse> response) {
                swithVisibility(false);
                if (response.code() != 200) {
                    showErrorDialog("Сервер временно недоступен. Повторите попытку позже");
                    return;
                }
                showCheckDialog(response.body().delta);
            }

            @Override
            public void onFailure(Call<DeltaResponse> call, Throwable t) {
                swithVisibility(false);
                showErrorDialog("Проверьте подключение к сети и повторите попытку позже");
            }
        });
    }

    private void showCheckDialog(Double delta) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Обновление данных")
                .setMessage(String.format("Размер обновления составляет %.2f Мб. Приступить к загрузке?", delta))
                .setPositiveButton("Загрузить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadContent();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void downloadContent() {
        tvUpdatingStatus.setText("Загрузка данных");
        swithVisibility(true);

        client.update(updatingHelper.getLastUpdatingTime()).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                try {
                    if (response.code() != 200) {
                        Log.e(LOG_TAG, String.format("Server error! Code: %d, Message: %s", response.code(), response.message()));
                        swithVisibility(false);
                        return;
                    }
                    UpdateResponse content = response.body();
                    tvUpdatingStatus.setText("Обновление базы");
                    Log.d(LOG_TAG, String.format("Получение данных успешно завершено! Получено категорий: %d, рецептов: %d, ингредиентов: %d", content.categories.size(), content.recipes.size(), content.ingredients.size()));
                    Log.d(LOG_TAG, "Сохранение данных");

                    //присваивает категориям иконки первого рецепта, входящего в неё
                    for (Category c : content.categories) {
                        for (Recipe r : content.recipes) {
                            if (r.categoryId == c.id) {
                                c.icon = r.icon;
                                break;
                            }
                        }
                    }

                    new DBCategoriesHelper(getContext()).addOrUpdate(content.categories);
                    DBIngredientsHelper dbIngredientsHelper = new DBIngredientsHelper(getContext());
                    dbIngredientsHelper.addOrReplace(content.ingredients);
                    dbIngredientsHelper.addOrReplacePairs(content.recIng);
                    new DBRecipesHelper(getContext()).addOrUpdate(content.recipes);


                    Log.d(LOG_TAG, "Сохранение завершено!");
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Ошибка при обновлении данных:", ex);
                } finally {
                    swithVisibility(false);
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Не удалось получить данные", t);
                swithVisibility(false);
            }
        });
    }


    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Не удалось получить данные")
                .setMessage(message)
                .setPositiveButton("Ок", null)
                .show();
    }

    private void swithVisibility(boolean updating) {
        if (updating) {
            layoutMain.setVisibility(View.GONE);
            layoutUpdating.setVisibility(View.VISIBLE);
        } else {
            layoutMain.setVisibility(View.VISIBLE);
            layoutUpdating.setVisibility(View.GONE);
        }
    }
}