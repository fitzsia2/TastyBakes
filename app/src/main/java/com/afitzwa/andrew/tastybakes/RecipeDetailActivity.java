package com.afitzwa.andrew.tastybakes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements IRecipeDetailFragment {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String STEP_FRAGMENT_TAG = "STEP_TAG";

    public static final String ARG_RECIPE_NAME_ID = "item_id";
    public static final String ARG_RECIPE_ROW_ID = "recipe_row_id";

    private static int mRecipeRowId;

    @BindView(R.id.detail_toolbar) Toolbar mToolbar;

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

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putInt(RecipeDetailFragment.ARG_RECIPE_ROW_ID, mRecipeRowId);
            arguments.putString(RecipeDetailFragment.ARG_RECIPE_NAME_ID, recipeName);

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_content, fragment, null)
                    .commit();
        }

        mToolbar.setTitle(recipeName);
    }

    @Override
    public void onStepSelected(int stepRowId) {
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(RecipeStepActivity.ARG_RECIPE_FK_ID, mRecipeRowId);
        intent.putExtra(RecipeStepActivity.ARG_RECIPE_STEP_ID, stepRowId);
        this.startActivity(intent);
    }
}
