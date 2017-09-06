package com.afitzwa.andrew.tastybakes.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Andrew on 9/1/17.
 */

public class StepColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String STEP_ORDER = "step_order";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SHORT_DESC = "short_description";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DESCRIPTION = "description";

    @DataType(DataType.Type.TEXT)
    public static final String VIDEO_URL = "video_url";

    @DataType(DataType.Type.TEXT)
    public static final String THUMB_URL = "thumb_url";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String RECIPE_FK = "recipe_fk";
}
