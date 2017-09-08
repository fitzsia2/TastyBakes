package com.afitzwa.andrew.tastybakes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.afitzwa.andrew.tastybakes.data.IngredientColumns;
import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepColumns;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew on 9/7/17.
 */

public class SaveRecipesToDBTask extends AsyncTask<String, Void, Void> {
    private final ISaveRecipesToDBTask mCaller;
    private final Context mContext;

    private boolean mNewRecipe = false;

    public SaveRecipesToDBTask(ISaveRecipesToDBTask caller, Context context) {
        mCaller = caller;
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String jsonString = strings[0];

        try {
            // Get all the listed recipes
            JSONArray recipesJSONArray = new JSONArray(jsonString);

            // Iterate through each recipe
            for (int ii = 0; ii < recipesJSONArray.length(); ii++) {

                JSONObject recipeJSON = (JSONObject) recipesJSONArray.get(ii);


                Cursor c = mContext.getContentResolver().query(RecipeProvider.Recipes.withName(recipeJSON.getString("name")), null, null, null, null);
                assert c != null;
                if (c.moveToFirst()) {
                    c.close();
                } else {

                    mNewRecipe = true;

                    ContentValues recipeValues = new ContentValues();
                    recipeValues.put(RecipeColumns.NAME, recipeJSON.getString("name"));
                    recipeValues.put(RecipeColumns.SERVINGS, recipeJSON.getInt("servings"));
                    recipeValues.put(RecipeColumns.IMAGE_URL, recipeJSON.getString("image"));

                    Uri recipeUri = mContext.getContentResolver().insert(
                            RecipeProvider.Recipes.CONTENT_URI, recipeValues);
                    assert recipeUri != null;

                    String recipeRowId = recipeUri.getLastPathSegment();


                    JSONArray ingredientArray = recipeJSON.getJSONArray("ingredients");
                    for (int uu = 0; uu < ingredientArray.length(); uu++) {
                        JSONObject ingredientJSON = (JSONObject) ingredientArray.get(uu);

                        ContentValues ingredientValues = new ContentValues();
                        ingredientValues.put(IngredientColumns.INGREDIENT, ingredientJSON.getString("ingredient"));
                        ingredientValues.put(IngredientColumns.MEASURE, ingredientJSON.getString("measure"));
                        ingredientValues.put(IngredientColumns.QUANTITY, ingredientJSON.getInt("quantity"));
                        ingredientValues.put(IngredientColumns.RECIPE_FK, recipeRowId);

                        mContext.getContentResolver().insert(
                                IngredientProvider.Ingredients.CONTENT_URI, ingredientValues);
                    }


                    JSONArray stepArray = recipeJSON.getJSONArray("steps");
                    for (int uu = 0; uu < stepArray.length(); uu++) {
                        JSONObject stepJSON = (JSONObject) stepArray.get(uu);

                        ContentValues stepValues = new ContentValues();
                        stepValues.put(StepColumns.DESCRIPTION, stepJSON.getString("description"));
                        stepValues.put(StepColumns.SHORT_DESC, stepJSON.getString("shortDescription"));
                        stepValues.put(StepColumns.THUMB_URL, stepJSON.getString("thumbnailURL"));
                        stepValues.put(StepColumns.VIDEO_URL, stepJSON.getString("videoURL"));
                        stepValues.put(StepColumns.RECIPE_FK, recipeRowId);
                        stepValues.put(StepColumns.STEP_ORDER, stepJSON.getInt("id"));

                        mContext.getContentResolver().insert(StepProvider.Steps.CONTENT_URI, stepValues);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mNewRecipe)
            mCaller.notifyNewData();
    }
}
