package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.IngredientColumns;

public class IngredientCursorAdapter extends CursorAdapter {
    private static final String TAG = IngredientCursorAdapter.class.getSimpleName();

    public IngredientCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        Log.v(TAG, "[newView]");
        return LayoutInflater.from(context).inflate(R.layout.ingredient, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor == null) {
            Log.e(TAG, "cursor is null!");
            return;
        }

        String ingredientAmount =
                cursor.getInt(cursor.getColumnIndexOrThrow(IngredientColumns.QUANTITY)) + " "
                        + cursor.getString(cursor.getColumnIndexOrThrow(IngredientColumns.MEASURE));

        String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(IngredientColumns.INGREDIENT));

        ((TextView) view.findViewById(R.id.ingredient_amount)).setText(ingredientAmount);
        ((TextView) view.findViewById(R.id.ingredient_name)).setText(ingredientName);
    }

    @Override
    public boolean isEnabled(int position) {
        return false; // disable selection
    }
}
