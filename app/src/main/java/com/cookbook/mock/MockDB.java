package com.cookbook.mock;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.cookbook.R;
import com.cookbook.helpers.BitmapHelper;
import com.cookbook.helpers.DBCategoriesHelper;
import com.cookbook.pojo.Category;

import java.util.ArrayList;
import java.util.Random;

public class MockDB {
    private static final String LOG_TAG = "CookBookMock";

    public static void createFakeDatabese(Context context) {
        DBCategoriesHelper dbCategoriesHelper = new DBCategoriesHelper(context);
        dbCategoriesHelper.addOrUpdate(getCategories(context,10));
        Log.w(LOG_TAG,"Создана фейковая база данных");
    }

    private static ArrayList<Category> getCategories(Context context, int count) {
        ArrayList<Category> categories = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_categories);
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Bitmap image = BitmapHelper.drawableToBitmap(context.getResources().getDrawable(R.drawable.ic_placeholder));
            categories.add(new Category(i, captions[random.nextInt(captions.length)], BitmapHelper.getBytes(image)));
        }
        return categories;
    }
}
