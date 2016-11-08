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

    //Test to make sure if the text fields are empty when the login button is clicked nothing happens
    @Test
    public void shouldStayOnLogin() {
        //Locate and click register button
        onView(withId(R.id.loginButton)).perform(click());

        //Check if popup message is displayed
        onView(withText("Missing email or password")).check(matches(isDisplayed()));
    }

    //Checks to see an email can be typed in and is saved
    @Test
    public void shouldEditEmailText() {
        //Type in a email address
        onView(withId(R.id.login_email_field)).perform(typeText(email));
        onView(withId(R.id.login_email_field)).check(matches(withText("email@email.com")));
    }

    //Checks whether password is valid
    @Test
    public void shouldNotAcceptPassword() {
        onView(withId(R.id.login_password_field)).perform(typeText("password"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Missing email or password")).check(matches(isDisplayed()));
    }

    //Checks to make sure a password can be typed in and is saved
    @Test
    public void shouldEditPasswordText() {
        //Type in a password
        onView(withId(R.id.login_password_field)).perform(typeText(password));
        onView(withId(R.id.login_password_field)).check(matches(withText("password12@Q")));
    }

    //Checks that the registration button leads to the registration page
    @Test
    public void clickRegistrationButton() {
        //Locate and click register button
        onView(withId(R.id.goToRegisterButton)).perform(click());

        //Check if registration screen is displayed
        onView(withId(R.id.activity_register)).check(matches(isDisplayed()));
    }

    //Checks to see if the loginbutton works and goes to the map screen
    @Test
    public void clickLoginButton() {
        //Set login and password
        onView(withId(R.id.login_email_field)).perform(typeText(email));
        onView(withId(R.id.login_password_field)).perform(typeText(password));


        //Locate and click login button
        onView(withId(R.id.loginButton)).perform(click());

        //Check if map screen is displayed
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));
    }
}
