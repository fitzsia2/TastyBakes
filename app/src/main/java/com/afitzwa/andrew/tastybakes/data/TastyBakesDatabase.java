package com.afitzwa.andrew.tastybakes.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = TastyBakesDatabase.VERSION)
public class TastyBakesDatabase {
    private TastyBakesDatabase() {
    }

    public static final int VERSION = 1;

    @Table(RecipeColumns.class)
    public static final String RECIPES = "recipes";

    @Table(IngredientColumns.class)
    public static final String INGREDIENTS = "ingredients";

    @Table(StepColumns.class)
    public static final String STEPS = "steps";
}
