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
        JSONArray recipes;
        try {
            // Get all the listed recipes
            recipes = new JSONArray(JsonData.data);

            // Iterate through each recipe
            for (int ii = 0; ii < recipes.length(); ii++) {
                JSONObject recipeJSON = (JSONObject) recipes.get(ii);
                RecipeContent.Recipe recipe = new RecipeContent
                        .Recipe(recipeJSON.getInt("id"), recipeJSON.getString("name"));

                recipe.setServings(recipeJSON.getInt("servings"));

                // Iterate through each ingredient of a recipe
                JSONArray ingredientArray = recipeJSON.getJSONArray("ingredients");
                for (int uu = 0; uu < ingredientArray.length(); uu++) {
                    JSONObject ingredientJSON = (JSONObject) ingredientArray.get(uu);
                    Ingredient ingredient = new Ingredient(
                            ingredientJSON.getString("ingredient"),
                            ingredientJSON.getString("measure"),
                            ingredientJSON.getInt("quantity"));
                    recipe.addIngredient(ingredient);
                }
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
        public static List<RecipeStep> mSteps = new ArrayList<>();
        private static List<Ingredient> mIngredients = new ArrayList<>();

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

        private void setServings(int mServings) {
            this.mServings = mServings;
        }

        private void addIngredient(Ingredient ingredient) {
            mIngredients.add(ingredient);
        }

        public List<Ingredient> getIngredients() {
            return mIngredients;
        }
    }
}
