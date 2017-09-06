package com.afitzwa.andrew.tastybakes.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;


public class RecipeColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull @Unique
    public static final String NAME = "name";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    public static final String IMAGE_URL = "image_url";
}
