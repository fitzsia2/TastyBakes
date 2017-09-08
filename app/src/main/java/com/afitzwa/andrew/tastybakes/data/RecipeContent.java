package com.afitzwa.andrew.tastybakes.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

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

    public static class Recipe {
        private int mId;
        private String mTitle;
        private int mServings;
        private String mImageUrl;

        private final List<RecipeStep> mSteps = new ArrayList<>();
        private final List<Ingredient> mIngredients = new ArrayList<>();

        public Recipe(int id, String title, int servings) {
            this.mId = id;
            this.mTitle = title;
            this.mServings = servings;
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
            private final String mTitle;           // A short title for this step
            private final String mRecipeTitle;     // Name of the recipe this step belongs to
            private final String mDescription;     // Detailed description of this step
            private final String mVideoURL;        // URL for this step instructional video
            private final String mThumbnailURL;    // URL of a thumbnail for this step
            private final int mId;

            private RecipeStep(String recipe, String shrtDes, String desc, String vidUrl, String mThmUrl, int id) {
                this.mRecipeTitle = recipe;
                this.mTitle = shrtDes;
                this.mDescription = desc;
                this.mVideoURL = vidUrl;
                this.mThumbnailURL = mThmUrl;
                this.mId = id;
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

            public int getId() {
                return mId;
            }
        }

        /**
         * Describes an ingredient in a recipe
         */
        public static class Ingredient {
            private final int mQuantity;
            private final String mMeasure;
            private final String mName;

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
