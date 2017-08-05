package com.afitzwa.andrew.tastybakes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RecipeContent.Recipe mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String key = getArguments().getString(ARG_ITEM_ID);
            mItem = RecipeContent.RECIPE_MAP.get(key);
            assert mItem != null;

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        LinearLayout recipeDetailView = rootView.findViewById(R.id.recipe_detail);

        // Build the ingredient list
        for (RecipeContent.Recipe.Ingredient ingredient : mItem.getIngredients()) {

            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.ingredients, container, false);

            ((TextView) ll.findViewById(R.id.ingredient_amount))
                    .setText(ingredient.getmQuantity() + " " + ingredient.getmMeasure());

            ((TextView) ll.findViewById(R.id.ingredient_name)).setText(ingredient.getmName());

            recipeDetailView.addView(ll);
        }

        // Build the step list
        for (RecipeContent.Recipe.RecipeStep step : mItem.mSteps) {

            recipeDetailView.addView(inflater.inflate(R.layout.list_divider, container, false));

            RelativeLayout stepLayout = (RelativeLayout) inflater.inflate(R.layout.recipe_step_view, container, false);

            ((TextView) stepLayout.findViewById(R.id.short_description_view)).setText(step.getShortDesc());

            ((TextView) stepLayout.findViewById(R.id.description_view)).setText(step.getDescription());

            recipeDetailView.addView(stepLayout);
        }

        return rootView;
    }
}
