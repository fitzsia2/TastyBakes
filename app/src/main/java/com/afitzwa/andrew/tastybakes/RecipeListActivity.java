package com.afitzwa.andrew.tastybakes;

import android.appwidget.AppWidgetManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.network.FetchUrlTask;
import com.afitzwa.andrew.tastybakes.network.IFetchUrlTask;
import com.afitzwa.andrew.tastybakes.widget.RecipeWidgetProvider;

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
public class RecipeListActivity extends AppCompatActivity
        implements IFetchUrlTask, ISaveRecipesToDBTask, android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private static final String RECIPE_URL = "http://go.udacity.com/android-baking-app-json";

    private static final int ARG_RECIPE_LOADER_ID = 0;

    @BindView(R.id.recipe_list) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Cursor mRecipesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecipesCursor = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);
        mRecyclerView.setAdapter(new RecipeRecyclerViewAdapter(mRecipesCursor));

        setSupportActionBar(mToolbar);

        FetchUrlTask fetchUrlTask = new FetchUrlTask(this);
        fetchUrlTask.execute(RECIPE_URL);
    }

    public void handleFetchUrlResult(String result) {
        Log.v(TAG, "[handleFetchUrlResult]");
        SaveRecipesToDBTask saveRecipesToDBTask = new SaveRecipesToDBTask(this, this);
        saveRecipesToDBTask.execute(result);
    }

    @Override
    protected void onDestroy() {
        if (mRecipesCursor != null)
            mRecipesCursor.close();
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, "[onLoadFinished]");
        mRecyclerView.setAdapter(new RecipeRecyclerViewAdapter(cursor));
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }


    // Called by SaveRecipesToDBTask
    @Override
    public void notifyNewData() {
        Log.v(TAG, "[notifyNewData]");

        getLoaderManager().initLoader(ARG_RECIPE_LOADER_ID, null, this);

        // Alert any widgets that we've updated recipes
        Intent intent = new Intent(this, RecipeWidgetProvider.class)
                .setPackage(this.getPackageName());
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(intent);
    }
}
