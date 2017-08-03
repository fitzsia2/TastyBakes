package com.afitzwa.andrew.tastybakes.data;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains all information for a recipe
 */

public class RecipeContent {
    private static final String TAG = RecipeContent.class.getSimpleName();

    public static final List<Recipe> RECIPES = new ArrayList<>();

    public static final Map<String, Recipe> RECIPE_MAP = new HashMap<>();

    static private void addRecipe(Recipe recipe) {
        RECIPES.add(recipe);
        RECIPE_MAP.put(recipe.getTitle(), recipe);
    }

    static {
        JSONArray json;
        try {
            json = new JSONArray(JsonData.data);

            for (int ii = 0; ii < json.length(); ii++) {
                JSONObject o = (JSONObject) json.get(ii);
                RecipeContent.Recipe recipe = new RecipeContent.Recipe(o.getInt("id"), o.getString("name"));
                recipe.setServings(o.getInt("servings"));
                addRecipe(recipe);
            }
        }catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e);
        }
    }

    public static class Recipe {
        private Recipe(int id, @NonNull String title) {
            this.mId = id;
            this.mTitle = title;
        }

        private int mId;
        private String mTitle;
        private int mServings;
        public static final List<RecipeStep> STEPS = new ArrayList<>();
        private static final List<Ingredient> mIngredient = new ArrayList<>();

        public int getId() {
            return mId;
        }

        public void setId(int mId) {
            this.mId = mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        public int getServings() {
            return mServings;
        }

        public void setServings(int mServings) {
            this.mServings = mServings;
        }
    }
}
