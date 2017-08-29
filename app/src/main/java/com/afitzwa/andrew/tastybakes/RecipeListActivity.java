package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;
import com.afitzwa.andrew.tastybakes.network.FetchUrlTask;
import com.afitzwa.andrew.tastybakes.network.IFetchUrlTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements IFetchUrlTask {
    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private static final String RECIPE_URL = "http://go.udacity.com/android-baking-app-json";

    @BindView(R.id.recipe_list) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        FetchUrlTask fetchUrlTask = new FetchUrlTask(this);
        fetchUrlTask.execute(RECIPE_URL);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(RecipeContent.RECIPES));
    }

    @Override
    public void handleFetchUrlResult(String result) {
        RecipeContent recipeContent = new RecipeContent();
        recipeContent.buildListFromJSONString(result);

        setupRecyclerView(mRecyclerView);
    }

    public class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeViewHolder> {

        private final List<RecipeContent.Recipe> RECIPES;

        private RecipeRecyclerViewAdapter(List<RecipeContent.Recipe> recipes) {
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

}
