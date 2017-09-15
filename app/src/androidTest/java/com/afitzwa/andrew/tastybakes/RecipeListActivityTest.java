package com.afitzwa.andrew.tastybakes;

import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

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

    Activity mActivity;

    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityRule =
            new IntentsTestRule<>(RecipeListActivity.class);


    @Before
    public void preTest() {
        mActivity = mActivityRule.getActivity();
        ContentProviderTestUtils.clearContentProvider(mActivity);
    }


    @Test
    public void clickRecipe_startActivity() {

        intending(isInternal());

        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(hasComponent(RecipeDetailActivity.class.getName())));
    }


    @After
    public void postTest() {
        ContentProviderTestUtils.clearContentProvider(mActivity);
    }
}
