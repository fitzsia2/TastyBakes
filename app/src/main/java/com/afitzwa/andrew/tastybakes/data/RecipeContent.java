package com.afitzwa.andrew.tastybakes.data;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
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
    public static List<Recipe> RECIPES = new ArrayList<>();

    public static final Map<String, Recipe> RECIPE_MAP = new HashMap<>();

    private void addRecipe(Recipe recipe) {
        if (RECIPE_MAP.get(recipe.getTitle()) == null) {
            RECIPES.add(recipe);
            RECIPE_MAP.put(recipe.getTitle(), recipe);
        }

    }

    public void buildListFromJSONString(Context context, String s) {
        JSONArray recipes;

        try {
            // Get all the listed recipes
            recipes = new JSONArray(s);

            // Iterate through each recipe
            for (int ii = 0; ii < recipes.length(); ii++) {

                JSONObject recipeJSON = (JSONObject) recipes.get(ii);

                RecipeContent.Recipe recipe =
                        new RecipeContent.Recipe(recipeJSON.getInt("id"), recipeJSON.getString("name"));

                recipe.setServings(recipeJSON.getInt("servings"));

                // Iterate through each ingredient and step of a recipe
                JSONArray ingredientArray = recipeJSON.getJSONArray("ingredients");
                JSONArray stepArray = recipeJSON.getJSONArray("steps");

                for (int uu = 0; uu < ingredientArray.length(); uu++) {
                    JSONObject ingredientJSON = (JSONObject) ingredientArray.get(uu);

                    Recipe.Ingredient ingredient = new Recipe.Ingredient(
                            ingredientJSON.getString("ingredient"),
                            ingredientJSON.getString("measure"),
                            ingredientJSON.getInt("quantity"));

                    recipe.addIngredient(ingredient);
                }

                for (int uu = 0; uu < stepArray.length(); uu++) {
                    JSONObject stepJSON = (JSONObject) stepArray.get(uu);

                    Recipe.RecipeStep step = new Recipe.RecipeStep(
                            recipe.getTitle(),
                            stepJSON.getString("shortDescription"),
                            stepJSON.getString("description"),
                            stepJSON.getString("videoURL"),
                            stepJSON.getString("thumbnailURL")
                    );

                    recipe.addStep(step);
                }


                ContentProviderOperation.Builder builder =
                        ContentProviderOperation.newUpdate(RecipeProvider.Recipes.CONTENT_URI);
                builder.withValue(RecipeColumns.NAME, recipe.getTitle());
                builder.withValue(RecipeColumns.SERVINGS, recipe.getServings());
                builder.withValue(RecipeColumns.IMAGE_URL, recipe.getmImageUrl());

                ArrayList<ContentProviderOperation> batchOperations = new ArrayList<ContentProviderOperation>();
                batchOperations.add(builder.build());

                ContentProviderResult[] results = context.getContentResolver()
                        .applyBatch(RecipeProvider.AUTHORITY, batchOperations);

                Log.v(TAG, "Applied " + results.length);

                addRecipe(recipe);
            }
        } catch (JSONException | RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public static class Recipe {
        private int mId;
        private String mTitle;
        private int mServings;
        private String mImageUrl;

        private List<RecipeStep> mSteps = new ArrayList<>();
        private List<Ingredient> mIngredients = new ArrayList<>();

        private Recipe(int id, String title) {
            this.mId = id;
            this.mTitle = title;
        }

        public List<RecipeStep> getSteps() {
            return mSteps;
        }

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
            this.mIngredients.add(ingredient);
        }

        private void addStep(RecipeStep step) {
            mSteps.add(step);
        }

        public List<Ingredient> getIngredients() {
            return mIngredients;
        }

        public String getmImageUrl() {
            return mImageUrl;
        }

        /**
         * Contains information about individual steps in a recipe
         */
        public static class RecipeStep {
            private String mTitle;           // A short title for this step
            private String mRecipeTitle;     // Name of the recipe this step belongs to
            private String mDescription;     // Detailed description of this step
            private String mVideoURL;        // URL for this step instructional video
            private String mThumbnailURL;    // URL of a thumbnail for this step

            private RecipeStep(String recipe, String shrtDes, String desc, String vidUrl, String mThmUrl) {
                this.mRecipeTitle = recipe;
                this.mTitle = shrtDes;
                this.mDescription = desc;
                this.mVideoURL = vidUrl;
                this.mThumbnailURL = mThmUrl;
            }

            public String getRecipeTitle() {
                return mRecipeTitle;
            }

            public String getShortDesc() {
                return mTitle;
            }

            public String getDescription() {
                return mDescription;
            }

            public String getVideoURL() {
                return mVideoURL;
            }

            public String getThumnailURL() {
                return mThumbnailURL;
            }
        }

        /**
         * Describes an ingredient in a recipe
         */
        public static class Ingredient {
            private int mQuantity;
            private String mMeasure;
            private String mName;

            private Ingredient(String name, String measure, int quantity) {
                this.mName = name;
                this.mMeasure = measure;
                this.mQuantity = quantity;
            }

            public int getQuantity() {
                return mQuantity;
            }

            public String getMeasure() {
                return mMeasure;
            }

            public String getName() {
                return mName;
            }

        }
    }
}
