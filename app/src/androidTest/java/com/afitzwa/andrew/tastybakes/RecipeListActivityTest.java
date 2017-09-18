package com.afitzwa.andrew.tastybakes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public IntentsTestRule<RecipeListActivity> mRule =
            new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void recipeListExists() {
        isDisplayed().matches(withId(R.id.recipe_list));
    }

    @Test
    public void clickRecipe_StartDetailActivity() {
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(toPackage("com.afitzwa.andrew.tastybakes"));
    }
}
