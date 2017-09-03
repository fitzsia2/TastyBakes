package com.afitzwa.andrew.tastybakes.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = IngredientProvider.AUTHORITY, database = TastyBakesDatabase.class)
public class IngredientProvider {
    public static final String AUTHORITY = "com.afitzwa.andrew.tastybakes.data.IngredientProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String INGREDIENTS = "ingredients";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = TastyBakesDatabase.INGREDIENTS)
    public static class Ingredients {
        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredients"
        )
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENTS);
    }
}
