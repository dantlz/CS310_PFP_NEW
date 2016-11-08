package com.pfp.parkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Alex on 11/7/2016.
 */

@RunWith(AndroidJUnit4.class)
public class EditListedSpacesTest {

    private String spaceName = "USC New/North Parking";
    private String spacePrice = "5";

    @Rule
    public final ActivityTestRule<EditListedSpacesActivity> editListedSpacesActivity =
                new ActivityTestRule<EditListedSpacesActivity>(EditListedSpacesActivity.class);

    @Test
    public void shouldEditListedSpaceName() {
        onView(withId(R.id.editName)).perform(typeText(spaceName));
        onView(withId(R.id.editName)).check(matches(withText("USC New/North Parking")));
    }

    @Test
    public void shouldEditListedSpacePrice() {
        onView(withId(R.id.editPrice)).perform(typeText(spacePrice));
        onView(withId(R.id.editPrice)).check(matches(withText("5")));
    }

}
