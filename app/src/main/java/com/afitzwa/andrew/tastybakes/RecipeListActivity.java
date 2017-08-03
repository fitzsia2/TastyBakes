package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afitzwa.andrew.tastybakes.data.RecipeContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(RecipeContent.RECIPES));
    }

    public class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

        private final List<RecipeContent.Recipe> mRecipes;

        public RecipeRecyclerViewAdapter(List<RecipeContent.Recipe> recipes) {
            mRecipes = recipes;
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeViewHolder holder, int position) {
            holder.mItem = mRecipes.get(position);
            holder.mIdView.setText(mRecipes.get(position).getTitle());
            holder.mContentView.setText(("Serves " + mRecipes.get(position).getServings()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Selected item: " + holder.mItem.getTitle(), Toast.LENGTH_SHORT)
                            .show();

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.getTitle());
                        RecipeDetailFragment fragment = new RecipeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.getTitle());

                        context.startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
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

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    /**
     * Taken from https://stackoverflow.com/questions/16233976/android-testing-accessing-json-file
     * @param fileName example: "data.json"
     * @param c application context
     * @return
     */
    public static String readFileFromAssets(String fileName, Context c) {
        try {
            InputStream is = c.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);

            return text;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
