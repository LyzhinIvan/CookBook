package com.cookbook.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.Pair;
import android.util.Log;

import com.cookbook.pojo.Ingredient;
import com.cookbook.pojo.Recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBSearchHelper extends DBRecipesHelper {

    public static class Builder {

        private DBSearchHelper instance;
        private String caption = null;
        private List<Ingredient> ingredients = null;
        private Long categoryId = -1L;
        private int cookingTime = -1;

        public Builder(Context context) {
            instance = new DBSearchHelper(context);
        }

        public Builder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder withIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder inCategory(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder withCookingTime(int cookingTime) {
            this.cookingTime = cookingTime;
            return this;
        }

        public Pair<Boolean, List<Long>> search() {
            return instance.findRecipes(caption, ingredients, categoryId, cookingTime);
        }
    }

    public DBSearchHelper(Context context) {
        super(context);
    }

    public Pair<Boolean, List<Long>> findRecipes(String recipeName, List<Ingredient> ings, Long categoryId, int cookingTime) {

        List<Recipe> recipes = ings != null ? getRecipes(ings) : getRecipes(recipeName); // находим рецепты с указанным именем или ингредиентами
        boolean exactlyFound = true;

        Set<Recipe> toRemove = new HashSet<>();
        for (Recipe r : recipes) {
            // если будут указаны и ингредиенты, и название рецепта, первоначальный поиск произойдет по ингредиентам
            // в таком случае, выполним фильтрацию по имени
            if (ings != null && recipeName != null && !r.name.contains(recipeName))
                toRemove.add(r);
            if (toRemove.size() == recipes.size() && ings != null) { // если после неё не осталось ниодного подходящего рецепта
                toRemove.clear(); // игнорируем ограничение по имени
                exactlyFound = false; //ставим соответствующий флаг
            }

            // производим поиск по доп. параметрам
            if (categoryId != -1 && r.categoryId != categoryId)
                toRemove.add(r);
            else if (cookingTime != -1 && r.cookingTime > cookingTime)
                toRemove.add(r);
        }
        recipes.removeAll(toRemove);
        List<Long> result = new ArrayList<>();
        for (Recipe r : recipes) {
            result.add(r.id);
        }
        return new Pair<>(exactlyFound, result);
    }

    /**
     * поиск id рецептов по названию и с любыми ингредиентами
     */
    public List<Recipe> getRecipes(String recipeName) {
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
     * поиск id рецептов с определенными компонентами
     */
    public List<Long> findRecipes(List<Ingredient> ings) {
        List<Recipe> recipes = getRecipes(ings);
        List<Long> rec = new ArrayList<>();
        for (Recipe r : recipes)
            rec.add(r.id);
        return rec;
    }

    /**
     * поиска id рецептов по названию и с определенными ингредиентвми
     */
    public List<Long> findRecipes(String recipeCaption, List<Ingredient> ings) {

        List<Recipe> list = getRecipes(ings);
        List<Long> res = new ArrayList<>();
        for (Recipe r : list) {
            if (r.name.toLowerCase().contains(recipeCaption.toLowerCase()))
                res.add(r.id);
        }
        return res;
    }

    // поиск рецептов с любым названием, но определенным списком ингредиентов
    private List<Recipe> getRecipes(List<Ingredient> ings) {

        ArrayList<Recipe> res = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("CREATE TEMP TABLE IF NOT EXISTS _ingFind(id integer)");

        for (Ingredient i : ings) {
            db.execSQL(String.format("insert into _ingFind(id) select %s ", i.id));
        }
        final String RECIPE_FIELDS = String.format("%s, %s, %s, %s, %s, %s, %s", RECIPE_ID, RECIPE_CAPTION, RECIPE_CATEGORY_ID, RECIPE_ICON, RECIPE_INSTRUCTION, RECIPE_SATIETY, RECIPE_TIME);
        Cursor c = db.rawQuery(String.format("select distinct %s from %s", RECIPE_FIELDS, TABLE_RECIPES) +
                String.format(" join (select * from %s join _ingFind on (%s == _ingFind.id))", TABLE_IR, IR_ING_ID) +
                String.format(" on (%s == %s)", RECIPE_ID, IR_REC_ID), null);

        bindRecipes(c, res);

        db.execSQL("DELETE FROM _ingFind");
        db.close();

        return res;
    }

    private void bindLongs(Cursor c, ArrayList<Long> recipes) {
        try {
            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(c.getColumnIndex(RECIPE_ID));
                    recipes.add(id);

                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "bindLongs error!");
        } finally {
            c.close();
        }
    }
}
