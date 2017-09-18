package com.afitzwa.andrew.tastybakes;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afitzwa.andrew.tastybakes.data.IngredientColumns;
import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.StepColumns;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ListView.OnItemClickListener {
    IRecipeDetailFragment mCaller;

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    public static final String ARG_RECIPE_NAME_ID = "item_id";
    public static final String ARG_RECIPE_ROW_ID = "recipe_row_id";

    private static final int CURSOR_LOADER_INGREDIENT_ID = 1;
    private static final int CURSOR_LOADER_STEP_ID = 2;

    private int mRecipeRowId;

    private Context mContext;

    private IngredientCursorAdapter mIngredientCursorAdapter;
    private StepCursorAdapter mStepCursorAdapter;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_RECIPE_ROW_ID)) {
            mRecipeRowId = getArguments().getInt(ARG_RECIPE_ROW_ID);
        }

        // onAttach will not be called on APIs lower than 23, leaving mContext blank
        if (mContext == null) {
            mContext = getActivity().getApplicationContext();
            try {
                mCaller = (IRecipeDetailFragment) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        mIngredientCursorAdapter = new IngredientCursorAdapter(mContext, null, 0);
        mStepCursorAdapter = new StepCursorAdapter(mContext, null, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_content, container, false);

        ListView ingredientListView = rootView.findViewById(R.id.ingredient_list_view);
        ingredientListView.setAdapter(mIngredientCursorAdapter);

        ListView stepListView = rootView.findViewById(R.id.step_list_view);
        stepListView.setAdapter(mStepCursorAdapter);
        stepListView.setOnItemClickListener(this);

        Bundle b = new Bundle();
        b.putInt(ARG_RECIPE_ROW_ID, mRecipeRowId);
        getLoaderManager().initLoader(CURSOR_LOADER_INGREDIENT_ID, b, this);
        getLoaderManager().initLoader(CURSOR_LOADER_STEP_ID, b, this);

        return rootView;
    }

    // Only called on (> API 22)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mCaller = (IRecipeDetailFragment) mContext;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        assert mContext != null;
        if (i == CURSOR_LOADER_INGREDIENT_ID) {
            return new CursorLoader(mContext,
                    IngredientProvider.Ingredients.CONTENT_URI,
                    null,
                    IngredientColumns.RECIPE_FK + " = " + mRecipeRowId,
                    null, null);
        } else if (i == CURSOR_LOADER_STEP_ID) {
            return new CursorLoader(mContext,
                    StepProvider.Steps.CONTENT_URI,
                    null,
                    StepColumns.RECIPE_FK + " = " + mRecipeRowId,
                    null, StepColumns.STEP_ORDER + " ASC");
        } else {
            Log.e(TAG, "[onCreateLoader] Invalid cursor loader id");
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursorLoader.getId() == CURSOR_LOADER_INGREDIENT_ID) {
            mIngredientCursorAdapter.swapCursor(cursor);
        } else if (cursorLoader.getId() == CURSOR_LOADER_STEP_ID) {
            mStepCursorAdapter.swapCursor(cursor);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        if (cursorLoader.getId() == CURSOR_LOADER_INGREDIENT_ID) {
            mIngredientCursorAdapter.swapCursor(null);
        } else if (cursorLoader.getId() == CURSOR_LOADER_STEP_ID) {
            mStepCursorAdapter.swapCursor(null);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mCaller.onStepSelected(i);
    }
}
