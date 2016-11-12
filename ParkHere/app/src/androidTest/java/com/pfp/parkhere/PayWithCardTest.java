package com.pfp.parkhere;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Alex on 11/7/2016.
 */

@RunWith(AndroidJUnit4.class)
public class PayWithCardTest {

    private String tooShortNumber = "123412341234123";
    private String correctNumber = "1234123412341234";
    private String tooLongNumber = "12341234123412345";

    private String tooShortDate = "123";
    private String correctDate = "0195";
    private String tooLongDate = "12345";

    private String tooShortCV = "12";
    private String correctCV = "123";
    private String tooLongCV = "1234";

    private String singleName = "AlexBradford";
    private String correctName = "Alex Bradford";

    @Test
    public void ensureCardNumberLength() {
        onView(withId(R.id.card_date_value)).perform(typeText(correctDate));
        onView(withId(R.id.card_cv_value)).perform(typeText(correctCV));
        onView(withId(R.id.card_name_value)).perform(typeText(correctName));
        onView(withText("Card number should be exactly 16 digits.\n")).equals(withParent(PayWithCardActivity.class));
        onView(withId(R.id.card_number_value)).perform(typeText(tooShortNumber));
        onView(withText("Card number should be exactly 16 digits.\n")).equals(withParent(PayWithCardActivity.class));
        onView(withId(R.id.card_number_value)).perform(typeText(tooLongNumber));

    }

    @Test
    public void ensureCardDateLength() {
        onView(withId(R.id.card_number_value)).perform(typeText(correctNumber));
        onView(withId(R.id.card_cv_value)).perform(typeText(correctCV));
        onView(withId(R.id.card_name_value)).perform(typeText(correctName));
        onView(withText("Card expiration date should be exactly 4 digits.\n")).equals(withParent(PayWithCardActivity.class));
        onView(withId(R.id.card_date_value)).perform(typeText(tooShortDate));
        onView(withText("Card expiration date should be exactly 4 digits.\n")).equals(withParent(PayWithCardActivity.class));
        onView(withId(R.id.card_date_value)).perform(typeText(tooLongDate));

    }

    @Test
    public void ensureCVCodeLength() {
        onView(withId(R.id.card_number_value)).perform(typeText(correctNumber));
        onView(withId(R.id.card_date_value)).perform(typeText(correctDate));
        onView(withId(R.id.card_name_value)).perform(typeText(correctName));

        onView(withId(R.id.card_cv_value)).perform(typeText(tooShortCV));
        onView(withText("Card CV code should be at exactly 3 digits.\n")).equals(withParent(PayWithCardActivity.class));
        onView(withId(R.id.card_cv_value)).perform(typeText(tooLongCV));
        onView(withText("Card CV code should be at exactly 3 digits.\n")).equals(withParent(PayWithCardActivity.class));

    }

    @Test
    public void ensureTypeFullName() {
        onView(withId(R.id.card_number_value)).perform(typeText(correctNumber));
        onView(withId(R.id.card_date_value)).perform(typeText(correctDate));
        onView(withId(R.id.card_cv_value)).perform(typeText(correctCV));

        onView(withId(R.id.card_name_value)).perform(typeText(singleName));
        onView(withText("Detected invalid name format. Enter first and last names of the cardholder.\n")).equals(withParent(PayWithCardActivity.class));

    }
}
