package com.cookbook.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DBRecipesHelper extends DBHelper {
    public DBRecipesHelper(Context context) {
        super(context);
    }

    public void addOrUpdate(List<Recipe> recipes) {
        if (recipes == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TABLE_RECIPES + " VALUES (?,?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        try {
            db.beginTransaction();
            for (Recipe r : recipes) {
                statement.clearBindings();
                statement.bindLong(1, r.id);
                statement.bindString(2, r.name);
                statement.bindLong(3, r.cookingTime);
                statement.bindLong(4, r.categoryId);
                bindBitmapOrNull(statement, 5, r.icon);
                statement.bindString(6, r.instruction);

                statement.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Ошибка при обновлении рецептов");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Recipe> getByCategory(long categoryId) {
        if (categoryId < 0)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s WHERE %s = '%d'", TABLE_RECIPES, RECIPE_CATEGORY_ID, categoryId);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<Recipe> recipes = new ArrayList<>();
        bindRecipes(c, recipes);

        db.close();
        return recipes;
    }

    public long getCount() {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_RECIPES);
    }

    public Recipe getById(long recipeId) {
        if (recipeId < 0)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s WHERE %s = '%d'", TABLE_RECIPES, RECIPE_ID, recipeId);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<Recipe> recipes = new ArrayList<>();
        bindRecipes(c, recipes);

        if (recipes.size() != 1) {
            Log.e(LOG_TAG, String.format("В базе найдено %d рецептов с id = %d", recipes.size(), recipeId));
        }

        db.close();
        return recipes.get(0);
    }

    public List<Recipe> getById(List<Long> ids) {
        if (ids == null)
            return null;

        String idsLine = TextUtils.join(", ", ids);

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s WHERE %s IN (%s)", TABLE_RECIPES, RECIPE_ID, idsLine);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<Recipe> recipes = new ArrayList<>();
        bindRecipes(c, recipes);

        db.close();
        return recipes;
    }


    public void addOrUpdate(final Recipe r) {
        addOrUpdate(new ArrayList<Recipe>() {{
            add(r);
        }});
    }

    protected void bindRecipes(Cursor c, ArrayList<Recipe> recipes) {
        try {
            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(c.getColumnIndex(RECIPE_ID));
                    String name = c.getString(c.getColumnIndex(RECIPE_CAPTION));
                    int time = c.getInt(c.getColumnIndex(RECIPE_TIME));
                    long catId = c.getLong(c.getColumnIndex(RECIPE_CATEGORY_ID));
                    String instruction = c.getString(c.getColumnIndex(RECIPE_INSTRUCTION));
                    byte[] iconBytes = c.getBlob(c.getColumnIndex(RECIPE_ICON));

                    recipes.add(new Recipe(id, name, time, catId, instruction, iconBytes));

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindRecipes error!");
        } finally {
            c.close();
        }
    }

}
