package com.afitzwa.andrew.tastybakes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

/**
 * Created by Andrew on 9/13/17.
 *
 * Add fake values to our content provider for testing.
 */

public final class ContentProviderTestUtils {
    public static void addFakeEntriesToProvider(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(RecipeColumns.NAME, "Blueberry Cheesecake");
        values.put(RecipeColumns.IMAGE_URL, "https://d30y9cdsu7xlg0.cloudfront.net/png/557-200.png");
        values.put(RecipeColumns.SERVINGS, 10);


        Uri recipeUri = activity.getContentResolver().insert(
                RecipeProvider.Recipes.CONTENT_URI, values
        );

        assert recipeUri != null;

        String recipeId = recipeUri.getLastPathSegment();
    }

    public static void clearContentProvider(Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();
        contentResolver.delete(RecipeProvider.Recipes.CONTENT_URI, null, null);
        contentResolver.delete(IngredientProvider.Ingredients.CONTENT_URI, null, null);
        contentResolver.delete(StepProvider.Steps.CONTENT_URI, null, null);
    }
}
