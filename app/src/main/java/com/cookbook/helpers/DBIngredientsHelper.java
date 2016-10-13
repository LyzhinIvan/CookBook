package com.cookbook.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.util.Pair;

import com.cookbook.mock.IngRecPair;
import com.cookbook.pojo.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class DBIngredientsHelper extends DBHelper {

    DBRecipesHelper dbRecipesHelper;

    public DBIngredientsHelper(Context context) {
        super(context);
        this.dbRecipesHelper = new DBRecipesHelper(context);
    }

    public List<Pair<Ingredient,String>> getByRecipeId(long recId) {
        if (recId < 0)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s WHERE %s = '%d'", TABLE_IR, IR_REC_ID, recId);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<Pair<Ingredient,String>> pairs = new ArrayList<>();
        bindPairs(c, pairs);

        db.close();
        return pairs;
    }

    public Ingredient getById(long ingId) {
        if (ingId < 0)
            return null;

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s WHERE %s = '%d'", TABLE_INGREDIENTS, ING_ID, ingId);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<Ingredient> ings = new ArrayList<>();
        bindIng(c, ings);

        db.close();
        return ings.get(0);
    }

    public void addOrReplace(final Ingredient i) {
        addOrReplace(new ArrayList<Ingredient>(){{add(i);}});
    }

    public void addOrReplace(List<Ingredient> ingredients) {
        if (ingredients == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TABLE_INGREDIENTS + " VALUES (?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        try {
            db.beginTransaction();
            for (Ingredient i : ingredients) {
                statement.clearBindings();
                statement.bindLong(1, i.id);
                statement.bindString(2, i.caption);

                statement.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Ошибка при обновлении ингредиента");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void addOrReplacePairs(List<IngRecPair> pairs) {
        if (pairs == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TABLE_IR + " VALUES (?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        try {
            db.beginTransaction();
            for (IngRecPair p : pairs) {
                statement.clearBindings();
                statement.bindLong(1, p.id);
                statement.bindLong(2, p.ingId);
                statement.bindLong(3, p.recId);
                statement.bindString(4, p.quantity);

                statement.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Ошибка при обновлении пар ингредиент - рецепт");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void bindPairs(Cursor c, ArrayList<Pair<Ingredient,String>> pairs) {
        try {
            if (c.moveToFirst()) {
                do {
                    String quantity = c.getString(c.getColumnIndex(IR_QUANTITY));
                    long ingId = c.getLong(c.getColumnIndex(IR_ING_ID));
                    Ingredient ing = getById(ingId);

                    pairs.add(new Pair<>(ing,quantity));

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindPairs error!");
        } finally {
            c.close();
        }
    }

    private void bindIng(Cursor c, ArrayList<Ingredient> ingredients) {
        try {
            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(c.getColumnIndex(ING_ID));
                    String name = c.getString(c.getColumnIndex(ING_CAPTION));

                    ingredients.add(new Ingredient(id, name));

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindIng error!");
        } finally {
            c.close();
        }
    }
}
