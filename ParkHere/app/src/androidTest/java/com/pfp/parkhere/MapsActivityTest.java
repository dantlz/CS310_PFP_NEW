package com.pfp.parkhere;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by lsteinhubl on 11/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    @Rule
    public final ActivityTestRule<MapsActivity> mapsActivity = new ActivityTestRule<>(MapsActivity.class);

    //Test to check it's being displayed
    @Test
    public void shouldLaunchMapScreen() {
        onView(withId(R.id.Owner)).check(matches(isDisplayed()));
    }

    
}
