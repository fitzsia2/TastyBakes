package com.afitzwa.andrew.tastybakes.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.afitzwa.andrew.tastybakes.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetProviderConfigureActivity RecipeWidgetProviderConfigureActivity}
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED = "com.afitzwa.andrew.tastybakes.widget.ACTION_DATA_UPDATED";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.v(TAG, "[updateAppWidget]");

        String packageName = context.getPackageName();
        RemoteViews widgetView =
                new RemoteViews(packageName, R.layout.recipe_widget_provider);


        widgetView.setEmptyView(R.id.widget_ingredient_list, R.id.widget_ingredient_empty_view);

        String recipe =
                RecipeWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);

        if (recipe != null) {
            Log.v(TAG, "[updateWidget]Recipe: " + recipe);
            widgetView.setTextViewText(R.id.widget_recipe_title_text_view, recipe);

            Intent intent = new Intent(context, RecipeRemoteViewsService.class);
            intent.putExtra(RecipeRemoteViewsService.RECIPE_TITLE_KEY, recipe);
            widgetView.setRemoteAdapter(R.id.widget_ingredient_list, intent);
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetView);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }
}

