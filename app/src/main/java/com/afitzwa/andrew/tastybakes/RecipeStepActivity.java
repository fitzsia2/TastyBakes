package com.afitzwa.andrew.tastybakes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RecipeStepActivity extends AppCompatActivity {
    public static final String ARG_RECIPE_FK_ID = "recipe_fk";
    public static final String ARG_RECIPE_STEP_ID = "step_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putInt(
                    RecipeStepFragment.ARG_STEP_ID,
                    getIntent().getIntExtra(RecipeStepActivity.ARG_RECIPE_STEP_ID, -1)
            );
            arguments.putInt(
                    RecipeStepFragment.ARG_RECIPE_FK_ID,
                    getIntent().getIntExtra(RecipeStepActivity.ARG_RECIPE_FK_ID, -1)
            );

            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, fragment)
                    .commit();
        }
    }
}
