package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.afitzwa.andrew.tastybakes.data.StepColumns;

class StepCursorAdapter extends CursorAdapter {
    private static final String TAG = StepCursorAdapter.class.getSimpleName();

    public StepCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.detail_step_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view).setText(cursor.getString(cursor.getColumnIndexOrThrow(StepColumns.SHORT_DESC)));
    }
}
