package com.afitzwa.andrew.tastybakes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * View holder for managing a recipe (listing recipes)
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private final View mView;
    private final TextView mRecipeNameView;
    private final TextView mContentView;
    private final ImageView mThumbNail;

    public View getView() {
        return mView;
    }

    public String getRecipeName() {
        return (String) mRecipeNameView.getText();
    }

    public void setRecipeName(String name) {
        mRecipeNameView.setText(name);
    }

    public ImageView getThumnailView() {
        return mThumbNail;
    }

    public void setContent(String content) {
        mContentView.setText(content);
    }

    public RecipeViewHolder(View view) {
        super(view);
        mView = view;
        mRecipeNameView = view.findViewById(R.id.recipe_title);
        mContentView = view.findViewById(R.id.content);
        mThumbNail = view.findViewById(R.id.recipe_icon);
    }
}
