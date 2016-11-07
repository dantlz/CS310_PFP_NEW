package com.pfp.parkhere;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.test.AndroidTestCase;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Created by lsteinhubl on 11/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule public final ActivityTestRule<LoginActivity> loginActivity = new ActivityTestRule<>(LoginActivity.class);

    //Test to check that LoginActivity is the first to be displayed
    @Test
    public void shouldLaunchLoginScreen() {
        onView(withId(R.id.login_email_field)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void typedInEmailField() {

//        onView(withId(R.id.login_email_field)).perform(typeText("email@email.com"), closeSoftKeyboard());

    }
}
