package com.cookbook.mock;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cookbook.R;
import com.cookbook.helpers.BitmapHelper;
import com.cookbook.helpers.DBCategoriesHelper;
import com.cookbook.helpers.DBIngredientsHelper;
import com.cookbook.helpers.DBRecipesHelper;
import com.cookbook.helpers.FavoritesHelper;
import com.cookbook.pojo.Category;
import com.cookbook.pojo.Ingredient;
import com.cookbook.pojo.Recipe;
import com.cookbook.pojo.Satiety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockDB {
    private static final String LOG_TAG = "CookBookMock";
    private static Random random = new Random();

    public static void createFakeDatabese(Context context) {

        Log.w(LOG_TAG, "Начинается создание фейковой БД");
        DBCategoriesHelper dbCategoriesHelper = new DBCategoriesHelper(context);
        DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(context);
        DBIngredientsHelper dbIngredientsHelper = new DBIngredientsHelper(context);

        // создаем 10 ингредиентов
        dbIngredientsHelper.addOrReplace(getIngredients(context,10));

        // создаем 10 категорий по 10 рецептов в каждой
        dbCategoriesHelper.addOrUpdate(getCategories(context, 10));
        for (int i = 0; i < 10; i++) {
            ArrayList<Recipe> recipes = getRecipes(context, i, 10);
            dbRecipesHelper.addOrUpdate(recipes);
        }

        // каждому рецепту добавляем по 3 ингредиента
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                List<IngRecPair> pairs = getIngRecPairs(i, j, 3);
                dbIngredientsHelper.addOrReplacePairs(pairs);
            }
        }

        // добавляем несколько рецептов в любимые
        FavoritesHelper favHelper = FavoritesHelper.getInstance(context);
        favHelper.addToFavorite(1);
        favHelper.addToFavorite(101);
        favHelper.addToFavorite(505);
        favHelper.addToFavorite(908);

        Log.w(LOG_TAG, "Создана фейковая база данных");
    }

    @NonNull
    private static List<IngRecPair> getIngRecPairs(int i, int j, int count) {
        List<IngRecPair> pairs = new ArrayList<>();
        for (int k = 0; k < count; k++) {
            IngRecPair ingRecPair = new IngRecPair(i * 100 + j * 10 + k, i * 100 + j, random.nextInt(10), getRandomQuantity());
            pairs.add(ingRecPair);
        }
        return pairs;
    }

    private static ArrayList<Category> getCategories(Context context, int count) {
        ArrayList<Category> categories = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_categories);

        for (int i = 0; i < count; i++) {
            Bitmap image = BitmapHelper.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_placeholder));
            categories.add(new Category(i, captions[random.nextInt(captions.length)], BitmapHelper.getBytes(image)));
        }
        return categories;
    }

    private static ArrayList<Recipe> getRecipes(Context context, int catId, int count) {
        ArrayList<Recipe> categories = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_recipes);

        for (int i = 0; i < count; i++) {
            String instruction = context.getResources().getString(R.string.lorem);
            Bitmap image = BitmapHelper.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_placeholder));
            categories.add(new Recipe(catId*100+i, captions[random.nextInt(captions.length)],(random.nextInt(6) + 1) * 15, getRandomSatiety(), catId, instruction, BitmapHelper.getBytes(image)));
        }
        return categories;
    }

    private static Satiety getRandomSatiety() {
        Satiety[] values = Satiety.values();
        return values[random.nextInt(values.length)];
    }

    private static String getRandomQuantity() {
        return ((random.nextInt(4)+1)*50)+" г.";
    }

    private static ArrayList<Ingredient> getIngredients(Context context, int count) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_ingredients);
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            ingredients.add(new Ingredient(i, captions[random.nextInt(captions.length)]));
        }
        return ingredients;
    }
}
