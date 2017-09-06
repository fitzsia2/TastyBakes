package com.afitzwa.andrew.tastybakes.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = RecipeProvider.AUTHORITY, database = TastyBakesDatabase.class)
public class RecipeProvider {
    public static final String AUTHORITY = "com.afitzwa.andrew.tastybakes.data.RecipeProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String NAMES = "names"; // of recipe
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = TastyBakesDatabase.RECIPES)
    public static class Recipes {
        @ContentUri(
                path = Path.NAMES,
                type = "vnd.android.cursor.dir/names"
        )
        public static final Uri CONTENT_URI = buildUri(Path.NAMES);

        @InexactContentUri(
                name = "RECIPE",
                path = Path.NAMES + "/*",
                type = "vnd.android.cursor.item/recipes",
                whereColumn = RecipeColumns.NAME,
                pathSegment = 1
        )
        public static Uri withName(String id) {
            return buildUri(Path.NAMES, id);
        }
    }
}
