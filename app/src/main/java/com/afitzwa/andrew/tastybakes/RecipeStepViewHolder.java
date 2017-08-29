package com.afitzwa.andrew.tastybakes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

/**
 * Created by Andrew on 8/29/17.
 */
public class RecipeStepViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mStepTextView;
    public RecipeContent.Recipe.RecipeStep mStep;

    public RecipeStepViewHolder(View view) {
        super(view);
        mView = view;
        mStepTextView = view.findViewById(R.id.short_description_view);
    }
}
