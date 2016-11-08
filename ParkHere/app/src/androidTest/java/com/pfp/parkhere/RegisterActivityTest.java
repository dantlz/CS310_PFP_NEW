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
public class RegisterActivityTest {

    private String first_name = "John";
    private String last_name = "Smith";
    private String email = "email@email.com";
    private String password = "Password123!";
    private String phone_number = "1234567";


    @Rule
    public final ActivityTestRule<RegisterActivity> registerActivity = new ActivityTestRule<>(RegisterActivity.class);

    //Check to make sure nothing happens if all/some of the fields are not filled in
    @Test
    public void shouldNotRegister() {
        //Click confirm with no fields filled in
        onView(withId(R.id.confirmButton)).perform(click());
        onView(withText("Registration faWiled")).check(matches(isDisplayed()));

        //Click confirm with some but not all fields filled in
        onView(withId(R.id.first_name_field)).perform(typeText(first_name));
        onView(withId(R.id.last_name_field)).perform(typeText(last_name));
        onView(withId(R.id.email_field)).perform(typeText(email));
        onView(withId(R.id.phone_field)).perform(typeText(phone_number));
        onView(withId(R.id.confirmButton)).perform(click());
        onView(withText("Registration failed")).check(matches(isDisplayed()));

    }

    //Test to make sure password must be > 10 characters
    @Test
    public void shouldNotAcceptShortPassword() {
        onView(withId(R.id.first_name_field)).perform(typeText(first_name));
        onView(withId(R.id.last_name_field)).perform(typeText(last_name));
        onView(withId(R.id.email_field)).perform(typeText(email));
        onView(withId(R.id.phone_field)).perform(typeText(phone_number));

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withId(R.id.password_field)).perform(typeText("password"));
        onView(withText("Password must be at least 10 characters")).check(matches(isDisplayed()));
    }

    //Test to make sure password must has a special character
    @Test
    public void shouldNotAcceptPasswordWithNoSpecial() {
        onView(withId(R.id.first_name_field)).perform(typeText(first_name));
        onView(withId(R.id.last_name_field)).perform(typeText(last_name));
        onView(withId(R.id.email_field)).perform(typeText(email));
        onView(withId(R.id.phone_field)).perform(typeText(phone_number));

        onView(withId(R.id.password_field)).perform(typeText("Password123212"));
        onView(withId(R.id.confirmButton)).perform(click());
        onView(withText("Password must contain at least one special character")).check(matches(isDisplayed()));
    }

    //Test to make sure the user uploaded a picture
    @Test
    public void shouldNotAcceptNoPicture() {
        onView(withId(R.id.first_name_field)).perform(typeText(first_name));
        onView(withId(R.id.last_name_field)).perform(typeText(last_name));
        onView(withId(R.id.email_field)).perform(typeText(email));
        onView(withId(R.id.phone_field)).perform(typeText(phone_number));
        onView(withId(R.id.password_field)).perform(typeText(password));
        onView(withId(R.id.repeat_password_field)).perform(typeText(password));

        onView(withId(R.id.confirmButton)).perform(click());
        onView(withText("Must set a display picture for your profile")).check(matches(isDisplayed()));
    }

}
