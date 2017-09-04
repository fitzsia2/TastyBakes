package com.afitzwa.andrew.tastybakes.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = StepProvider.AUTHORITY, database = TastyBakesDatabase.class)
public class StepProvider {
    public static final String AUTHORITY = "com.afitzwa.andrew.tastybakes.data.StepProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String STEPS = "steps";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = TastyBakesDatabase.STEPS)
    public static class Steps {
        @ContentUri(
                path = Path.STEPS,
                type = "vnd.android.cursor.dir/steps"
        )
        public static final Uri CONTENT_URI = buildUri(Path.STEPS);

        @InexactContentUri(
                name = "STEP_ID",
                path = Path.STEPS + "/*",
                type = "vnd.android.cursor.item/steps",
                whereColumn = StepColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(Integer i) {
            return buildUri(Path.STEPS, i.toString());
        }
    }
}
