package com.cookbook.dummy;


import android.content.Context;

import com.cookbook.R;
import com.cookbook.pojo.Ingredient;
import com.cookbook.pojo.Recipe;
import com.cookbook.pojo.Satiety;

import java.util.ArrayList;
import java.util.Random;

public class DummyRecipes {

    private static Random random = new Random();

    public static ArrayList<Recipe> getRecipes(Context context, int count) {
        ArrayList<Recipe> categories = new ArrayList<>();
        String[] captions = context.getResources().getStringArray(R.array.dummy_categories);

        for (int i = 0; i < count; i++) {
            ArrayList<Ingredient> ingredients = DummyIngredients.getIngredients(context, 3);
            categories.add(new Recipe(captions[random.nextInt(captions.length)],getRandomSatiety(),(random.nextInt(6)+1)*15, ingredients));
        }
        return categories;
    }

    private static Satiety getRandomSatiety() {
        Satiety[] values = Satiety.values();
        return values[random.nextInt(values.length)];
    }
}
