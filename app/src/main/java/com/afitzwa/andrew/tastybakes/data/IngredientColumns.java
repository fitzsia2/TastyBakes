package com.afitzwa.andrew.tastybakes.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Andrew on 9/1/17.
 */

public class IngredientColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String INGREDIENT = "ingredient";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String MEASURE = "measure";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String RECIPE_FK = "recipe_fk";
}
