package com.afitzwa.andrew.tastybakes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.afitzwa.andrew.tastybakes.data.IngredientColumns;
import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

/**
 * Created by Andrew on 9/13/17.
 * <p>
 * Add fake values to our content provider for testing.
 */

public final class ContentProviderTestUtils {
    public static final String RECIPE_NAME = "Blueberry Cheesecake";
    public static final String RECIPE_URL = "https://d30y9cdsu7xlg0.cloudfront.net/png/557-200.png";
    public static final int RECIPE_SERVINGS = 8;
    public static final int INGREDIENT_AMOUNT = 2;
    public static final String INGREDIENT_NAME = "flour";
    public static final String INGREDIENT_MEASURE = "flour";

    public static void addFakeEntriesToProvider(Activity activity) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeColumns.NAME, RECIPE_NAME);
        recipeValues.put(RecipeColumns.IMAGE_URL, RECIPE_URL);
        recipeValues.put(RecipeColumns.SERVINGS, RECIPE_SERVINGS);


        Uri recipeUri = activity.getContentResolver().insert(
                RecipeProvider.Recipes.CONTENT_URI, recipeValues
        );

        assert recipeUri != null;

        String recipeId = recipeUri.getLastPathSegment();

        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(IngredientColumns.RECIPE_FK, recipeId);
        ingredientValues.put(IngredientColumns.QUANTITY, INGREDIENT_AMOUNT);
        ingredientValues.put(IngredientColumns.MEASURE, INGREDIENT_MEASURE);
        ingredientValues.put(IngredientColumns.INGREDIENT, INGREDIENT_NAME);
        activity.getContentResolver().insert(
                IngredientProvider.Ingredients.CONTENT_URI, ingredientValues);

    }

    public static void clearContentProvider(Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();
        contentResolver.delete(RecipeProvider.Recipes.CONTENT_URI, null, null);
        contentResolver.delete(IngredientProvider.Ingredients.CONTENT_URI, null, null);
        contentResolver.delete(StepProvider.Steps.CONTENT_URI, null, null);
    }
}
