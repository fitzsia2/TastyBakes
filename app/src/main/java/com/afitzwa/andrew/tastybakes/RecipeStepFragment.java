package com.afitzwa.andrew.tastybakes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import static com.afitzwa.andrew.tastybakes.data.RecipeContent.RECIPE_MAP;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepFragment extends Fragment {
    public static final String ARG_RECIPE_ID = "item_id";
    public static final String ARG_RECIPE_STEP_ID = "recipe_step_id";

    public RecipeStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            String recipeName = bundle.getString(ARG_RECIPE_ID);
            int stepInstruction = bundle.getInt(ARG_RECIPE_STEP_ID);
            RecipeContent.Recipe recipe = RECIPE_MAP.get(recipeName);
            RecipeContent.Recipe.RecipeStep recipeStep = recipe.getSteps().get(stepInstruction);
            ((TextView) view.findViewById(R.id.recipe_step_detail)).setText(recipeStep.getDescription());
        }

        return view;
    }
}
