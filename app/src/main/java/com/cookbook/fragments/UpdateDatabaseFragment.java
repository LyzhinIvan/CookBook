package com.cookbook.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import com.cookbook.MainActivity;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


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
        long recipesCount = new DBRecipesHelper(getContext()).getCount();
        tvRecipesInBase.setText(Long.toString(recipesCount));

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

        client.getDelta(updatingHelper.getLastUpdatingTime() / 1000).enqueue(new Callback<DeltaResponse>() {
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
        double eps = 0.01;
        if (delta > eps) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_check_dialog_caption)
                    .setMessage(String.format(getString(R.string.text_confirm_download), delta))
                    .setPositiveButton("Загрузить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadContent();
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_check_dialog_caption)
                    .setMessage(R.string.text_download_not_need)
                    .setPositiveButton("Ок", null)
                    .show();
        }
    }

    private void downloadContent() {
        tvUpdatingStatus.setText("Загрузка данных");
        swithVisibility(true);

        client.update(updatingHelper.getLastUpdatingTime() / 1000).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                try {
                    if (response.code() != 200) {
                        Log.e(LOG_TAG, String.format("Server error! Code: %d, Message: %s", response.code(), response.message()));
                        swithVisibility(false);
                        return;
                    }
                    UpdateResponse content = response.body();
                    Log.d(LOG_TAG, String.format("Получение данных успешно завершено! Получено категорий: %d, рецептов: %d, ингредиентов: %d", content.categories.size(), content.recipes.size(), content.ingredients.size()));
                    tvUpdatingStatus.setText("Обновление базы");

                    UpdateDatabaseTask.TaskResult onResult = new UpdateDatabaseTask.TaskResult() {
                        @Override
                        public void onComplete(Boolean success) {
                            swithVisibility(false);
                            if (success) {
                                MainActivity activity = (MainActivity) getActivity();
                                activity.setFragment(new CategoriesFragment(), false);
                            }
                        }
                    };

                    UpdateDatabaseTask task = new UpdateDatabaseTask(getContext(), onResult, content);
                    task.execute();
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Ошибка при обновлении данных:", ex);
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

    private static class UpdateDatabaseTask extends AsyncTask<Void, Void, Boolean> {

        interface TaskResult {
            void onComplete(Boolean result);
        }

        private final Context context;
        private final TaskResult taskResult;
        private UpdateResponse content;

        public UpdateDatabaseTask(Context context, TaskResult onResult, UpdateResponse content) {
            this.context = context;
            taskResult = onResult;
            this.content = content;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(LOG_TAG, "Сохранение данных");

            try {
                UpdatingHelper updatingHelper = new UpdatingHelper(context);
                updatingHelper.setLastUpdatingTime(content.newUpdated * 1000);
                new DBCategoriesHelper(context).addOrUpdate(content.categories);
                DBIngredientsHelper dbIngredientsHelper = new DBIngredientsHelper(context);
                dbIngredientsHelper.addOrReplace(content.ingredients);
                dbIngredientsHelper.addOrReplacePairs(content.recIng);
                new DBRecipesHelper(context).addOrUpdate(content.recipes);

                updateCategoriesIcon();

                Log.d(LOG_TAG, "Сохранение завершено!");
                return true;
            }
            catch (Exception ex) {
                Log.e(TAG, "doInBackground: ", ex);
                return false;
            }
        }

        private void updateCategoriesIcon() {
            DBCategoriesHelper dbCategoriesHelper = new DBCategoriesHelper(context);
            DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(context);
            for (Category c : dbCategoriesHelper.getAll()) {
                List<Recipe> recipes = dbRecipesHelper.getByCategory(c.id);
                if (recipes != null && recipes.size() != 0) {
                    c.icon = recipes.get(0).icon;
                    dbCategoriesHelper.addOrUpdate(c);
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            taskResult.onComplete(success);
        }
    }
}