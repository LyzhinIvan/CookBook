package com.cookbook.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cookbook.pojo.Ingredient;
import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DBSearchHelper extends DBRecipesHelper {

    public DBSearchHelper(Context context) {
        super(context);
    }

    /**
     * поиск рецептов по названию и с любыми ингредиентами
     */
    public List<Recipe> findRecipes(String recipeName) {
        if (recipeName.isEmpty())
            return new ArrayList<>();

        final String query = String.format("select * from %1$s where %2$s like '%%%3$s%%' ORDER BY %2$s", TABLE_RECIPES, RECIPE_CAPTION, recipeName);
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        ArrayList<Recipe> recipes = new ArrayList<>();
        bindRecipes(c, recipes);

        db.close();
        return recipes;
    }

    /**
     * поиск рецептов с любым названием, но определенным списком ингредиентов
     */
    public List<Recipe> findRecipes(List<Ingredient> ings) {

        ArrayList<Recipe> res = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("CREATE TEMP TABLE IF NOT EXISTS _ingFind(id integer)");

        for (Ingredient i : ings) {
            db.execSQL(String.format("insert into _ingFind(id) select %s ", i.id));
        }

        Cursor c = db.rawQuery(String.format("select distinct * from %s", TABLE_RECIPES) +
                String.format(" join (select * from %s join _ingFind on (%s == _ingFind.id))", TABLE_IR, IR_ING_ID) +
                String.format(" on (%s == %s)", RECIPE_ID, IR_REC_ID), null);

        bindRecipes(c, res);

        db.execSQL("DELETE FROM _ingFind");
        db.close();

        return res;
    }


}
