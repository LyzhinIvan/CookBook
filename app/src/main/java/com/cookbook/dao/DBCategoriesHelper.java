package com.cookbook.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.cookbook.pojo.Category;

import java.util.ArrayList;
import java.util.List;

public class DBCategoriesHelper extends DBHelper {

    public DBCategoriesHelper(Context context) {
        super(context);
    }

    public void addOrUpdate(List<Category> categories) {
        if (categories == null)
            return;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TABLE_CATEGORIES + " VALUES (?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);

        try {
            db.beginTransaction();
            for (Category c : categories) {
                statement.clearBindings();
                statement.bindLong(1, c.id);
                statement.bindString(2, c.name);
                //statement.bindBlob(3, BitmapHelper.getBytes(c.icon));
                bindBitmapOrNull(statement,3,c.icon);
                statement.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Ошибка при обновлении категории");
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void addOrUpdate(final Category c) {
        addOrUpdate(new ArrayList<Category>() {{
            add(c);
        }});
    }

    public List<Category> getAll() {
        SQLiteDatabase db = getReadableDatabase();

        String FIND_REQUEST_QUERY = String.format("SELECT * FROM %s", TABLE_CATEGORIES);
        Cursor c = db.rawQuery(FIND_REQUEST_QUERY, null);

        ArrayList<Category> categories = new ArrayList<>();
        bindCategories(c, categories);

        db.close();
        return categories;
    }

    private void bindCategories(Cursor c, ArrayList<Category> categories) {
        try {
            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(c.getColumnIndex(CATEGORY_ID));
                    String name = c.getString(c.getColumnIndex(CATEGORY_CAPTION));
                    byte[] iconBytes = c.getBlob(c.getColumnIndex(CATEGORY_ICON));

                    categories.add(new Category(id, name, iconBytes));

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindCategories error!");
        } finally {
            c.close();
        }
    }
}
