package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {
    private static final String TAG = RecipeListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(RecipeContent.RECIPES));
    }

    public class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

        private final List<RecipeContent.Recipe> RECIPES;

        public RecipeRecyclerViewAdapter(List<RecipeContent.Recipe> recipes) {
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
            holder.mItem = RECIPES.get(position);
            holder.mIdView.setText(RECIPES.get(position).getTitle());
            holder.mContentView.setText(("Serves " + RECIPES.get(position).getServings()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailActivity.ARG_ITEM_ID, holder.mItem.getTitle());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return RECIPES.size();
        }

        public class RecipeViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public RecipeContent.Recipe mItem;

            public RecipeViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.recipe_title);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
