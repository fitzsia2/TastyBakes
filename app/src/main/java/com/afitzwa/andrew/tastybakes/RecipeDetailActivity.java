package com.afitzwa.andrew.tastybakes;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity
        implements IRecipeDetailFragment, IRecipeStepFragment {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String STEP_FRAGMENT_TAG = "STEP_TAG";

    public static final String ARG_RECIPE_NAME_ID = "item_id";
    public static final String ARG_RECIPE_ROW_ID = "recipe_row_id";

    private static int mRecipeRowId;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        String recipeName = getIntent().getStringExtra(RecipeDetailActivity.ARG_RECIPE_NAME_ID);
        mRecipeRowId = getIntent().getIntExtra(RecipeDetailActivity.ARG_RECIPE_ROW_ID, -1);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipeName);
        }

        mTwoPane = getResources().getBoolean(R.bool.has_two_panes);

        if (savedInstanceState == null) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putInt(RecipeDetailFragment.ARG_RECIPE_ROW_ID, mRecipeRowId);
            arguments.putString(RecipeDetailFragment.ARG_RECIPE_NAME_ID, recipeName);

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, fragment, STEP_FRAGMENT_TAG)
                    .commit();

            if (mTwoPane && (findViewById(R.id.step_fragment) != null)) {
                addStepDetailFragment(mRecipeRowId, 0);
            }

        } else {
            Fragment stepFragment = getFragmentManager().findFragmentByTag(STEP_FRAGMENT_TAG);
            if (stepFragment == null) {
                addStepDetailFragment(mRecipeRowId, 0);
            }
        }
    }

    private void addStepDetailFragment(int recipeFK, int stepId) {
        Log.d(TAG, "Adding step fragment");
        Bundle stepArgs = new Bundle();
        stepArgs.putInt(RecipeStepFragment.ARG_RECIPE_FK_ID, recipeFK);
        stepArgs.putInt(RecipeStepFragment.ARG_STEP_ID, stepId);

        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setArguments(stepArgs);

        getFragmentManager().beginTransaction()
                .replace(R.id.step_fragment, stepFragment)
                .commit();

    }


    @Override
    public void onStepSelected(int stepRowId) {
        if (mTwoPane) {
            Bundle stepArgs = new Bundle();
            stepArgs.putInt(RecipeStepFragment.ARG_RECIPE_FK_ID, mRecipeRowId);
            stepArgs.putInt(RecipeStepFragment.ARG_STEP_ID, stepRowId);

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setArguments(stepArgs);

            getFragmentManager().beginTransaction()
                    .replace(R.id.step_fragment, stepFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(RecipeStepActivity.ARG_RECIPE_FK_ID, mRecipeRowId);
            intent.putExtra(RecipeStepActivity.ARG_RECIPE_STEP_ID, stepRowId);
            this.startActivity(intent);

        }
    }

    @Override
    public void onStepNavigation(int stepId) {
        onStepSelected(stepId);
    }
}
