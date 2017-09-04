package com.afitzwa.andrew.tastybakes;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeContent;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepProvider;
import com.afitzwa.andrew.tastybakes.network.FetchUrlTask;
import com.afitzwa.andrew.tastybakes.network.IFetchUrlTask;
import com.afitzwa.andrew.tastybakes.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements IFetchUrlTask {
    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private static final String RECIPE_URL = "http://go.udacity.com/android-baking-app-json";

    @BindView(R.id.recipe_list) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        FetchUrlTask fetchUrlTask = new FetchUrlTask(this);
        fetchUrlTask.execute(RECIPE_URL);
    }

    public void handleFetchUrlResult(String result) {
        RecipeContent recipeContent = new RecipeContent();
        recipeContent.buildListFromJSONString(getContentResolver(), result);

        // Alert any widgets that we've updated recipes
        Intent intent = new Intent(this, RecipeWidgetProvider.class)
                .setPackage(this.getPackageName());
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(intent);

        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);
        assert cursor != null;

        setupRecyclerView(mRecyclerView, cursor);
    }

    public static void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull Cursor cursor) {
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(cursor));
    }

    @Override
    protected void onDestroy() {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(RecipeProvider.Recipes.CONTENT_URI, null, null);
        contentResolver.delete(IngredientProvider.Ingredients.CONTENT_URI, null, null);
        contentResolver.delete(StepProvider.Steps.CONTENT_URI, null, null);
        super.onDestroy();
    }
}
