package com.pfp.parkhere;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by lsteinhubl on 11/6/16.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    String email = "email@email.com";
    String password = "password12@Q";


    @Rule public final ActivityTestRule<LoginActivity> loginActivity = new ActivityTestRule<>(LoginActivity.class);

    //Test to check that LoginActivity is the first to be displayed
    @Test
    public void shouldLaunchLoginScreen() {
        onView(withId(R.id.login_email_field)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldEditEmailText() {
        //Type in a email address
        onView(withId(R.id.login_email_field)).perform(typeText(email));
        onView(withId(R.id.login_email_field)).check(matches(withText("email@email.com")));
    }

    @Test
    public void shouldEditPasswordText() {
        //Type in a password
        onView(withId(R.id.login_password_field)).perform(typeText(password));
        onView(withId(R.id.login_password_field)).check(matches(withText("password12@Q")));
    }

    @Test
    public void clickRegistrationButton() {
        //Locate and click register button
        onView(withId(R.id.goToRegisterButton)).perform(click());

        //Check if registration screen is displayed
        onView(withId(R.id.activity_register)).check(matches(isDisplayed()));
    }

    @Test
    public void clickLoginButton() {
        //Locate and click login button
        onView(withId(R.id.loginButton)).perform(click());

        //Check if map screen is displayed
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));
    }
}
