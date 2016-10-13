package com.cookbook.dummy;


import android.content.Context;
import android.graphics.Bitmap;

import com.cookbook.R;
import com.cookbook.helpers.BitmapHelper;
import com.cookbook.pojo.Category;
import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.Random;

public class DummyCategories {
    public static ArrayList<Category> getCategories(Context context, int count, int recipesPerCategory) {
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
