package com.afitzwa.andrew.tastybakes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.afitzwa.andrew.tastybakes.data.RecipeColumns;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Andrew on 9/15/17.
 * <p>
 * Tests the detail activity.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    private static final String TAG = DetailActivityTest.class.getSimpleName();

    private String mRecipe;
    private int mRow;

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mRule = new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {

        {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Cursor c = targetContext.getContentResolver().query(
                    RecipeProvider.Recipes.CONTENT_URI, null, null, null, null);
            assert c != null;
            c.moveToFirst();
            mRecipe = c.getString(c.getColumnIndexOrThrow(RecipeColumns.NAME));
            mRow = c.getInt(c.getColumnIndexOrThrow(RecipeColumns._ID));
        }

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Bundle b = new Bundle();
            b.putString(RecipeDetailActivity.ARG_RECIPE_NAME_ID, mRecipe);
            b.putInt(RecipeDetailActivity.ARG_RECIPE_ROW_ID, mRow);
            Intent i = new Intent(targetContext, RecipeDetailActivity.class);
            i.putExtras(b);
            return i;
        }

    };

    @Test
    public void listViewsExist() {
        isDisplayed().matches(withId(R.id.ingredient_list_view));
        isDisplayed().matches(withId(R.id.step_list_view));
    }

    @Test
    public void clickIntroductionStep_LaunchStepDetailActivity() {
        SystemClock.sleep(500); // Make sure cursor loads data into list


        onData(anything()).inAdapterView(withId(R.id.step_list_view)).atPosition(0).perform(click());

        // if the step_fragment_container is displayed, a new activity will not be launched
        try {
            onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));
            Log.v(TAG, "Running in two-pane mode, skipping intent check");
        } catch (NoMatchingViewException e) {
            Log.v(TAG, "Running in single-pane mode, checking intent");
            intended(allOf(
                    toPackage("com.afitzwa.andrew.tastybakes"),
                    hasExtras(allOf(
                            hasEntry(equalTo(RecipeStepActivity.ARG_RECIPE_FK_ID), equalTo(mRow)),
                            hasEntry(equalTo(RecipeStepActivity.ARG_RECIPE_STEP_ID), equalTo(0))
                    ))
            ));
        }
    }
}
