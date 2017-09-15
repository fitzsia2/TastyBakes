package com.afitzwa.andrew.tastybakes.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.afitzwa.andrew.tastybakes.R;
import com.afitzwa.andrew.tastybakes.data.IngredientColumns;
import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;

import static com.afitzwa.andrew.tastybakes.R.layout.ingredient;

/**
 * Provides views for widget
 */

public class RecipeRemoteViewsService extends RemoteViewsService {
    public static final String TAG = RecipeRemoteViewsService.class.getSimpleName();
    public static final String RECIPE_TITLE_KEY = "recipe_title_key";

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory() {

            private final String[] COL_SELECTION = new String[]{
                    IngredientColumns.INGREDIENT,
                    IngredientColumns.QUANTITY,
                    IngredientColumns.MEASURE,
            };

            private Cursor mCursor;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (mCursor != null)
                    mCursor.close();

                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                String recipe = intent.getExtras().getString(RECIPE_TITLE_KEY, null);

                Cursor recipeCursor =  getContentResolver().query(
                        RecipeProvider.Recipes.CONTENT_URI,
                        null, RecipeColumns.NAME + " = " + recipe, null, null);

                if (recipeCursor != null) {

                    if (recipeCursor.moveToFirst()) {

                        int idCol = recipeCursor.getColumnIndexOrThrow(RecipeColumns._ID);

                        int recipeId = recipeCursor.getInt(idCol);

                        mCursor = getContentResolver().query(IngredientProvider.Ingredients.CONTENT_URI,
                                COL_SELECTION,
                                IngredientColumns.RECIPE_FK + " = " + recipeId,
                                null, null);

                    } else {
                        Log.w(TAG, "[onDataSetChanged] Could not get ingredients for " + recipe);
                    }

                    recipeCursor.close();

                } else {
                    Log.e(TAG, "[onDataSetChanged] Could not get recipe cursor");
                }


                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (mCursor != null)
                    mCursor.close();
            }

            @Override
            public int getCount() {
                if (mCursor != null) {
                    return mCursor.getCount();
                } else {
                    Log.w(TAG, "[getCount] Cursor null");
                    return 0;
                }
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (i == AdapterView.INVALID_POSITION || mCursor == null) {
                    return null;
                }
                String packageName = getPackageName();
                RemoteViews views = new RemoteViews(packageName, ingredient);

                mCursor.moveToPosition(i);

                int ingredColId = mCursor.getColumnIndexOrThrow(IngredientColumns.INGREDIENT);
                int measureColId = mCursor.getColumnIndexOrThrow(IngredientColumns.MEASURE);
                int amountColId = mCursor.getColumnIndexOrThrow(IngredientColumns.QUANTITY);
                String ingredientName = mCursor.getString(ingredColId);
                String measure = mCursor.getString(measureColId);
                int amount = mCursor.getInt(amountColId);

                views.setTextViewText(R.id.ingredient_amount, amount + " " + measure);
                views.setTextViewText(R.id.ingredient_name, ingredientName);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
