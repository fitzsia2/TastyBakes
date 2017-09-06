package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afitzwa.andrew.tastybakes.data.RecipeColumns;

/**
 * Recycler View Adapter used for managing the list of recipes
 */
public class RecipeRecyclerViewAdapter
        extends RecyclerView.Adapter<RecipeViewHolder> {

    public static final String TAG = RecipeRecyclerViewAdapter.class.getSimpleName();

    final Cursor mCursor;

    RecipeRecyclerViewAdapter(Cursor c) {
        mCursor = c;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        final int rowId = mCursor.getInt(mCursor.getColumnIndexOrThrow(RecipeColumns._ID));

        String recipeName = mCursor.getString(mCursor.getColumnIndexOrThrow(RecipeColumns.NAME));
        int servings = mCursor.getInt(mCursor.getColumnIndexOrThrow(RecipeColumns.SERVINGS));

        holder.setRecipeName(recipeName);
        holder.setContent("Serves " + servings);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.ARG_RECIPE_ROW_ID, rowId);
                intent.putExtra(RecipeDetailActivity.ARG_RECIPE_NAME_ID, holder.getRecipeName());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
