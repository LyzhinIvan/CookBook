package com.cookbook.dummy;

import android.content.Context;

import com.cookbook.R;
import com.cookbook.pojo.Ingredient;

import java.util.ArrayList;
import java.util.Random;


public class DummyIngredients {
    public static ArrayList<Ingredient> getIngredients(Context context, int count) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_ingredients);
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            ingredients.add(new Ingredient(captions[random.nextInt(captions.length)],((random.nextInt(4)+1)*50)+" г."));
        }
        return ingredients;
    }
}
