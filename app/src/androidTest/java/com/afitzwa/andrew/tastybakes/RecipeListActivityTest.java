package com.afitzwa.andrew.tastybakes;

import android.content.ContentResolver;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.afitzwa.andrew.tastybakes.data.IngredientProvider;
import com.afitzwa.andrew.tastybakes.data.RecipeProvider;
import com.afitzwa.andrew.tastybakes.data.StepProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityRule =
            new IntentsTestRule<>(RecipeListActivity.class);


    @Before
    public void preTest() {
        clearContentProvider();
    }


    @Test
    public void clickRecipe_startActivity() {

        intending(isInternal());

        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(hasComponent(RecipeDetailActivity.class.getName())));
    }


    @After
    public void postTest() {
        clearContentProvider();
    }

    private void clearContentProvider() {
        ContentResolver contentResolver = mActivityRule.getActivity().getContentResolver();
        contentResolver.delete(RecipeProvider.Recipes.CONTENT_URI, null, null);
        contentResolver.delete(IngredientProvider.Ingredients.CONTENT_URI, null, null);
        contentResolver.delete(StepProvider.Steps.CONTENT_URI, null, null);
    }
}
