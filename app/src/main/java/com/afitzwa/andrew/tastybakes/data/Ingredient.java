package com.afitzwa.andrew.tastybakes.data;

import android.support.annotation.NonNull;

/**
 * Describes an ingredient in a recipe
 */
public class Ingredient {
    private int mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(@NonNull String mName) {
        this.mName = mName;
    }
}
