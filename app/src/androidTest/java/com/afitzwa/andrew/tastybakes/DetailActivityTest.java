package com.afitzwa.andrew.tastybakes;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Andrew on 9/15/17.
 *
 * Tests the detail activity.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Test
    public void ingredientsListExists() {
        isDisplayed().matches(withId(R.id.ingredient_list_view));
    }

    @Test
    public void stepListExists() {
        isDisplayed().matches(withId(R.id.step_list_view));
    }
}
