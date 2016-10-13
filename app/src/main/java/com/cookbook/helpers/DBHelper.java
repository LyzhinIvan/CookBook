package com.cookbook.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    // Названия таблиц
    protected static final String
        TABLE_CATEGORIES = "Categories",
        TABLE_INGREDIENTS = "Ingredients",
        TABLE_RECIPES = "Recipes",
        TABLE_IR = "ingredientsToRecipes";

    // Заголовки полей таблиц
    protected static final String
        //Категории
        CATEGORY_ID = "id",
        CATEGORY_CAPTION = "caption",
        CATEGORY_ICON = "icon",

        //Ингредиенты
        ING_ID = "id",
        ING_CAPTION = "caption",

        //Рецепты
        RECIPE_ID = "id",
        RECIPE_CAPTION = "caption",
        RECIPE_TIME = "time",
        RECIPE_CATEGORY_ID = "category_id",
        RECIPE_ICON = "icon",
        RECIPE_INSTRUCTION = "instruction",

        //Связь Ингредиент - рецепт
        IR_ID = "id",
        IR_ING_ID = "ing_id",
        IR_REC_ID = "rec_id",
        IR_QUANTITY = "quantity";

    // Создание таблиц
    protected static final String
            CREATE_CATEGORIES_TABLE =
            "create table "+ TABLE_CATEGORIES +" ("
                    + CATEGORY_ID +" INTEGER PRIMARY KEY,"
                    + CATEGORY_CAPTION +" varchar(255) NOT NULL,"
                    + CATEGORY_ICON + " BLOB"
                    + ");",

            CREATE_INGREDIENTS_TABLE =
            "create table "+ TABLE_INGREDIENTS +" ("
                + ING_ID +" INTEGER PRIMARY KEY,"
                + ING_CAPTION +" varchar(255) NOT NULL"
                + ");",

            CREATE_RECIPES_TABLE =
            "create table "+ TABLE_RECIPES +" ("
                    + RECIPE_ID +" INTEGER PRIMARY KEY,"
                    + RECIPE_CAPTION +" varchar(255) NOT NULL,"
                    + RECIPE_TIME +" INTEGER NOT NULL,"
                    + RECIPE_CATEGORY_ID +" INTEGER NOT NULL,"
                    + RECIPE_ICON +" BLOB,"
                    + RECIPE_INSTRUCTION +" TEXT NOT NULL,"
                    + " FOREIGN KEY ("+RECIPE_CATEGORY_ID+") REFERENCES "+TABLE_CATEGORIES + "("+CATEGORY_ID+")"
                    + ");",

            CREATE_IR_TABLE =
            "create table "+ TABLE_IR +" ("
                    + IR_ID +" INTEGER PRIMARY KEY,"
                    + IR_ING_ID +" INTEGER NOT NULL,"
                    + IR_REC_ID +" INTEGER NOT NULL,"
                    + IR_QUANTITY +" TEXT NOT NULL,"
                    + "FOREIGN KEY ("+IR_ING_ID+") REFERENCES "+TABLE_INGREDIENTS + "("+ING_ID+"),"
                    + "FOREIGN KEY ("+IR_REC_ID+") REFERENCES "+TABLE_RECIPES + "("+RECIPE_ID+")"
                    + ");";

    protected static final String LOG_TAG = "dbCookbook";

    public static final String DB_NAME = "CookBook";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_IR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_INGREDIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_RECIPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_IR_TABLE);
    }

    public void clearAll() {
        clearTable(TABLE_IR);
        clearTable(TABLE_RECIPES);
        clearTable(TABLE_INGREDIENTS);
        clearTable(TABLE_CATEGORIES);
    }

    private void clearTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }
}
