package com.cookbook.mock;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Base64;
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

    public static void createFakeDatabase(Context context) {

        Log.w(LOG_TAG, "Начинается создание фейковой БД");
        DBCategoriesHelper dbCategoriesHelper = new DBCategoriesHelper(context);
        DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(context);
        DBIngredientsHelper dbIngredientsHelper = new DBIngredientsHelper(context);

        // создаем 10 ингредиентов
        dbIngredientsHelper.addOrReplace(getIngredients(context, 10));

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

    public static void createTestDatabase(Context context) {
        Log.w(LOG_TAG, "Начинается создание тестовой БД");

        DBCategoriesHelper dbCategoriesHelper = new DBCategoriesHelper(context);
        DBRecipesHelper dbRecipesHelper = new DBRecipesHelper(context);
        DBIngredientsHelper dbIngredientsHelper = new DBIngredientsHelper(context);

        final byte[] bytes = Base64.decode(context.getResources().getString(R.string.testImg), Base64.DEFAULT);

        ArrayList<Category> cats = new ArrayList<Category>() {{
            add(new Category(0, "Блюда из курицы", bytes));
            add(new Category(1, "Закуски", bytes));
        }};
        dbCategoriesHelper.addOrUpdate(cats);

        ArrayList<Ingredient> ings = new ArrayList<Ingredient>() {{
            add(new Ingredient(0, "курица сырая"));
            add(new Ingredient(1, "кабачок"));
            add(new Ingredient(2, "острый перец"));
            add(new Ingredient(3, "помидоры"));
        }};
        dbIngredientsHelper.addOrReplace(ings);

        ArrayList<Recipe> recipes = new ArrayList<Recipe>() {{
            add(new Recipe(0, "Острая курица с кабачками", 45, Satiety.Medium, 0, "", bytes));
            add(new Recipe(1, "Курица по-кавказски", 30, Satiety.Medium, 0, "", bytes));
            add(new Recipe(3, "Вареная курица", 45, Satiety.Medium, 0, "", bytes));
            add(new Recipe(2, "Острые кабачки", 45, Satiety.Medium, 1, "", bytes));
            add(new Recipe(4, "Кабачки, тушеные с томатами", 45, Satiety.Medium, 1, "", bytes));
        }};
        dbRecipesHelper.addOrUpdate(recipes);

        ArrayList<IngRecPair> pairs = new ArrayList<IngRecPair>() {{
            //Острая курица с кабачками
            add(new IngRecPair(0, 0, 0, "1 шт"));
            add(new IngRecPair(1, 0, 1, "500 г."));
            add(new IngRecPair(2, 0, 2, "на кончике ножа"));

            //Курица по-кавказски
            add(new IngRecPair(3, 1, 0, "1 шт"));
            add(new IngRecPair(4, 1, 2, "2 чайные ложки"));

            //Острые кабачки
            add(new IngRecPair(5, 2, 1, "1 кг."));
            add(new IngRecPair(6, 2, 2, "по вкусу"));

            //Вареная курица
            add(new IngRecPair(7, 3, 0, "1 кг."));

            //Кабачки, тушеные с томатами
            add(new IngRecPair(8, 4, 1, "500 г."));
            add(new IngRecPair(9, 4, 3, "3 шт."));
        }};
        dbIngredientsHelper.addOrReplacePairs(pairs);
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
            categories.add(new Recipe(catId * 100 + i, captions[random.nextInt(captions.length)], (random.nextInt(6) + 1) * 15, getRandomSatiety(), catId, instruction, BitmapHelper.getBytes(image)));
        }
        return categories;
    }

    private static Satiety getRandomSatiety() {
        Satiety[] values = Satiety.values();
        return values[random.nextInt(values.length)];
    }

    private static String getRandomQuantity() {
        return ((random.nextInt(4) + 1) * 50) + " г.";
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
