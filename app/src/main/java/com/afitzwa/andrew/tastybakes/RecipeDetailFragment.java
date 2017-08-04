package com.afitzwa.andrew.tastybakes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        for (RecipeContent.Recipe.Ingredient ingredient : mItem.getIngredients()) {

            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.ingredients, container, false);

            ((TextView) ll.findViewById(R.id.ingredient_amount))
                    .setText(ingredient.getmQuantity() + " " + ingredient.getmMeasure());

            ((TextView) ll.findViewById(R.id.ingredient_name)).setText(ingredient.getmName());

            recipeDetailView.addView(ll);
        }


        for (RecipeContent.Recipe.RecipeStep step : mItem.mSteps) {

            recipeDetailView.addView(inflater.inflate(R.layout.list_divider, container, false));

            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.recipe_step_view, container, false);

            ((TextView) ll.findViewById(R.id.short_description_view)).setText(step.getShortDesc());

            ((TextView) ll.findViewById(R.id.description_view)).setText(step.getDescription());

            if (step.getThumnailURL() != null) {
                // TODO load thumbnail
            }

            recipeDetailView.addView(ll);
        }

        return rootView;
    }
}
