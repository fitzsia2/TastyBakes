package com.afitzwa.andrew.tastybakes.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.afitzwa.andrew.tastybakes.R;
import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import static com.afitzwa.andrew.tastybakes.data.RecipeContent.RECIPE_MAP;

/**
 * Provides views for widget
 */

public class RecipeRemoteViewsService extends RemoteViewsService {
    public static final String TAG = RecipeRemoteViewsService.class.getSimpleName();
    public static final String RECIPE_TITLE_KEY = "recipe_title_key";

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory() {
            private RecipeContent.Recipe mRecipe;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {
                mRecipe = null;
            }

            @Override
            public int getCount() {
                String recipeTitle = intent.getExtras().getString(RECIPE_TITLE_KEY);
                mRecipe = RECIPE_MAP.get(recipeTitle);
                if (mRecipe != null) {
                    return mRecipe.getIngredients().size();
                }
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (i == AdapterView.INVALID_POSITION || mRecipe == null) {
                    return null;
                }
                String packageName = getPackageName();
                RemoteViews views = new RemoteViews(packageName, R.layout.ingredient);
                views.setTextViewText(R.id.ingredient_amount, mRecipe.getIngredients().get(i).getName());

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
