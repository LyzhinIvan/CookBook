package com.cookbook.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.cookbook.pojo.Recipe;
import com.cookbook.pojo.Satiety;

import java.util.ArrayList;
import java.util.List;

public class DBShopListHelper extends DBHelper {

    public DBShopListHelper(Context context) {
        super(context);
    }

    public List<String> getAll() {

        SQLiteDatabase db = getReadableDatabase();

        String FIND_QUERY = String.format("SELECT * FROM %s", TABLE_SHOP_LIST);
        Cursor c = db.rawQuery(FIND_QUERY, null);

        ArrayList<String> lines = new ArrayList<>();
        bindLines(c, lines);

        db.close();
        return lines;
    }

    private void bindLines(Cursor c, ArrayList<String> lines) {
        try {
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(c.getColumnIndex(SHOP_LIST_NAME));
                    lines.add(name);

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindLines error!");
        } finally {
            c.close();
        }
    }

    public void add(String ing) {
        if (ing == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TABLE_SHOP_LIST + " VALUES (?);";
        SQLiteStatement statement = db.compileStatement(sql);

        try {
            db.beginTransaction();
            statement.bindString(1, ing);
            statement.execute();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Ошибка при обновлении списка покупок", ex);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void remove(String ing) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (db.delete(TABLE_SHOP_LIST, SHOP_LIST_NAME + "='" + ing + "'", null) != 1) {
                throw new Exception(String.format("Not deleted ing = %s", ing));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
