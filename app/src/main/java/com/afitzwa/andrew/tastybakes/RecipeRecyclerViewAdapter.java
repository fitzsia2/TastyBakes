package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import java.util.List;

/**
 * Recycler View Adapter used for managing the list of recipes
 */
public class RecipeRecyclerViewAdapter
        extends RecyclerView.Adapter<RecipeViewHolder> {

    private final List<RecipeContent.Recipe> RECIPES;

    RecipeRecyclerViewAdapter(List<RecipeContent.Recipe> recipes) {
        RECIPES = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        holder.setRecipe(RECIPES.get(position));
        holder.getIdView().setText(RECIPES.get(position).getTitle());
        holder.getContentView().setText(("Serves " + RECIPES.get(position).getServings()));

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.ARG_ITEM_ID, holder.getRecipe().getTitle());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RECIPES.size();
    }

}
