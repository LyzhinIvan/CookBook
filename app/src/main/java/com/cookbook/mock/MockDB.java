package com.cookbook.mock;

import android.content.Context;

public class MockDB {
    private static final String LOG_TAG = "CookBookMock";

    public static void createTestDatabase(Context context) {
        /*Log.w(LOG_TAG, "Начинается создание тестовой БД");

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
            add(new Recipe(0, "Острая курица с кабачками", 45, 0, "", bytes));
            add(new Recipe(1, "Курица по-кавказски", 30, 0, "", bytes));
            add(new Recipe(3, "Вареная курица", 45, 0, "", bytes));
            add(new Recipe(2, "Острые кабачки", 45, 1, "", bytes));
            add(new Recipe(4, "Кабачки, тушеные с томатами", 45, 1, "", bytes));
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
        dbIngredientsHelper.addOrReplacePairs(pairs);*/
    }
}
