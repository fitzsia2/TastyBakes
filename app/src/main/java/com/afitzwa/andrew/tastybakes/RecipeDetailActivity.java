package com.afitzwa.andrew.tastybakes;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.IngredientColumns;
import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeContent;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepColumns;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

import java.util.List;

import static com.afitzwa.andrew.tastybakes.data.RecipeContent.RECIPE_MAP;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String STEP_FRAGMENT_TAG = "STEP_TAG";
    private static final int RECIPE_CURSOR_LOADER_ID = 0;
    private static final int INGREDIENT_CURSOR_LOADER_ID = 1;
    private static final int STEP_CURSOR_LOADER_ID = 2;

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_RECIPE_NAME_ID = "item_id";
    public static final String ARG_RECIPE_KEY_ID = "recipe_key_id";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecipeContent.Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle b = new Bundle();
        b.putString(
                ARG_RECIPE_NAME_ID,
                getIntent().getStringExtra(RecipeDetailActivity.ARG_ITEM_ID));

        getLoaderManager().initLoader(RECIPE_CURSOR_LOADER_ID, b, this);

        mRecipe = RECIPE_MAP.get(getIntent().getStringExtra(RecipeDetailActivity.ARG_ITEM_ID));

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle(mRecipe.getTitle());
        setSupportActionBar(toolbar);

        RecyclerView recipeDetailRecyclerView = findViewById(R.id.recipe_detail);
        assert recipeDetailRecyclerView != null;
        assert mRecipe != null;
        setupRecyclerView(recipeDetailRecyclerView, mRecipe);

        if (findViewById(R.id.recipe_step_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RecipeContent.Recipe recipe) {
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new DetailRecyclerViewAdapter(recipe.getSteps()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == RECIPE_CURSOR_LOADER_ID) {
            return new CursorLoader(this,
                    RecipeProvider.Recipes.withName(bundle.getString(ARG_RECIPE_NAME_ID)),
                    null, null, null, null);
        } else if (i == INGREDIENT_CURSOR_LOADER_ID) {
            return new CursorLoader(this,
                    IngredientProvider.Ingredients.CONTENT_URI,
                    null,
                    IngredientColumns.RECIPE_FK + " = " + bundle.getInt(ARG_RECIPE_KEY_ID),
                    null, null);
        } else if (i == STEP_CURSOR_LOADER_ID) {
            return new CursorLoader(this,
                    StepProvider.Steps.CONTENT_URI,
                    null,
                    StepColumns.RECIPE_FK + " = " + bundle.getInt(ARG_RECIPE_KEY_ID),
                    null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case RECIPE_CURSOR_LOADER_ID:
                Log.v(TAG, "[onLoadFinished] Recipe cursor loaded");
                c.moveToFirst();
                Bundle b = new Bundle();
                b.putInt(ARG_RECIPE_KEY_ID, c.getInt(c.getColumnIndexOrThrow(RecipeColumns._ID)));
                getLoaderManager().initLoader(INGREDIENT_CURSOR_LOADER_ID, b, this);
                getLoaderManager().initLoader(STEP_CURSOR_LOADER_ID, b, this);
                break;
            case INGREDIENT_CURSOR_LOADER_ID:
                Log.v(TAG, "[onLoadFinished] Ingredient cursor loaded");
                break;
            case STEP_CURSOR_LOADER_ID:
                Log.v(TAG, "[onLoadFinished] Step cursor loaded");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class DetailRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeStepViewHolder> {

        private static final int VIEW_TYPE_INGREDIENTS = 0;
        private static final int VIEW_TYPE_STEP = 1;

        private final List<RecipeContent.Recipe.RecipeStep> RECIPE_STEP;

        public DetailRecyclerViewAdapter(List<RecipeContent.Recipe.RecipeStep> recipeStep) {
            RECIPE_STEP = recipeStep;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return VIEW_TYPE_INGREDIENTS;
            else
                return VIEW_TYPE_STEP;
        }

        @Override
        public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case VIEW_TYPE_INGREDIENTS:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.ingredient_card, parent, false);
                    break;
                case VIEW_TYPE_STEP:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recipe_step_list_content, parent, false);
                    break;
                default:
                    // ERROR
                    break;
            }
            return new RecipeStepViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
            if (getItemViewType(position) == VIEW_TYPE_INGREDIENTS) {

                holder.mStep = RECIPE_STEP.get(position);

                List<RecipeContent.Recipe.Ingredient> ingredients =
                        RECIPE_MAP.get(holder.mStep.getRecipeTitle()).getIngredients();

                for (RecipeContent.Recipe.Ingredient ingredient : ingredients) {

                    RelativeLayout ingredientLayout =
                            (RelativeLayout) View.inflate(getApplicationContext(), R.layout.ingredient, null);

                    TextView ingredientAmount = ingredientLayout.findViewById(R.id.ingredient_amount);
                    TextView ingredientName = ingredientLayout.findViewById(R.id.ingredient_name);

                    ingredientAmount.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
                    ingredientName.setText(ingredient.getName());

                    ((LinearLayout) holder.mView).addView(ingredientLayout);
                }
            } else {
                holder.mStep = RECIPE_STEP.get(position - 1);

                assert holder.mStep != null;

                holder.mStepTextView.setText(holder.mStep.getShortDesc());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mTwoPane) {
                            Bundle args = new Bundle();
                            args.putString(RecipeStepFragment.ARG_RECIPE_ID, holder.mStep.getRecipeTitle());
                            args.putInt(RecipeStepFragment.ARG_RECIPE_STEP_ID, holder.getAdapterPosition() - 1);

                            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                            recipeStepFragment.setArguments(args);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipe_step_container, recipeStepFragment, STEP_FRAGMENT_TAG)
                                    .commit();
                        } else {

                            Context context = view.getContext();
                            Intent intent = new Intent(context, RecipeStepActivity.class);
                            intent.putExtra(RecipeStepActivity.ARG_RECIPE_ID, holder.mStep.getRecipeTitle());
                            intent.putExtra(RecipeStepActivity.ARG_RECIPE_STEP_ID, holder.getAdapterPosition() - 1);

                            context.startActivity(intent);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return RECIPE_STEP.size() + 1;
        } // +1 for ingredient list

    }

}
