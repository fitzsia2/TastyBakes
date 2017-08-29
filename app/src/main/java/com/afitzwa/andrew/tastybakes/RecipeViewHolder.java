package com.afitzwa.andrew.tastybakes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

/**
 * View holder for managing a recipe (listing recipes)
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private final TextView mIdView;
    private final TextView mContentView;
    private RecipeContent.Recipe mRecipe;

    public TextView getIdView() {
        return mIdView;
    }

    public View getView() {
        return mView;
    }

    public TextView getContentView() {
        return mContentView;
    }

    public RecipeContent.Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(RecipeContent.Recipe mRecipe) {
        this.mRecipe = mRecipe;
    }

    RecipeViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = view.findViewById(R.id.recipe_title);
        mContentView = view.findViewById(R.id.content);
    }
}
