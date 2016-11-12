package com.cookbook.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.util.Log;

import com.cookbook.helpers.BitmapHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Названия таблиц
    protected static final String
        TABLE_CATEGORIES = "Categories",
        TABLE_INGREDIENTS = "Ingredients",
        TABLE_RECIPES = "Recipes",
        TABLE_IR = "IngRec",
        TABLE_SHOP_LIST = "ShopList";

    // Заголовки полей таблиц
    protected static final String
        //Категории
        CATEGORY_ID = "cat_id",
        CATEGORY_CAPTION = "caption",
        CATEGORY_ICON = "icon",

        //Ингредиенты
        ING_ID = "ing_id",
        ING_CAPTION = "ing_caption",

        //Рецепты
        RECIPE_ID = "rec_id",
        RECIPE_CAPTION = "rec_caption",
        RECIPE_TIME = "rec_time",
        RECIPE_CATEGORY_ID = "rec_category_id",
        RECIPE_ICON = "rec_icon",
        RECIPE_INSTRUCTION = "rec_instruction",

        //Связь Ингредиент - рецепт
        IR_ID = "ir_id",
        IR_ING_ID = "ir_ing_id",
        IR_REC_ID = "ir_rec_id",
        IR_QUANTITY = "ir_quantity",

        //Список покупок
        SHOP_LIST_NAME = "sl_name";

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
                    + ");",

            CREATE_SHOP_LIST_TABLE =
                    "create table "+ TABLE_SHOP_LIST +" ("
                            + SHOP_LIST_NAME +" varchar(255) PRIMARY KEY );";

    protected static final String LOG_TAG = "dbCookbook";
    public static final String DB_NAME = "CookBook";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    protected void bindBitmapOrNull(SQLiteStatement statement, int index, Bitmap img) {
        if (img==null)
            statement.bindNull(index);
        else statement.bindBlob(index, BitmapHelper.getBytes(img));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_IR_TABLE);
        db.execSQL(CREATE_SHOP_LIST_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_INGREDIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_RECIPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_IR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_SHOP_LIST_TABLE);
    }

    public void clearAll() {
        clearTable(TABLE_IR);
        clearTable(TABLE_RECIPES);
        clearTable(TABLE_INGREDIENTS);
        clearTable(TABLE_CATEGORIES);
        clearTable(TABLE_SHOP_LIST);
    }

    protected void clearTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }
}
