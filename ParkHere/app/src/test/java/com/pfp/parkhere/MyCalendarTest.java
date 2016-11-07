package com.pfp.parkhere;

import android.content.Context;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ObjectClasses.MyCalendar;

/**
 * Created by tshih on 11/6/16.
 */

public class MyCalendarTest {
    //private static final String FAKE_STRING = "HELLO WORLD";

    @Mock
    Context mMockContext;

    @Test
    public void myCalendarConstructorTest() {
        // Y2k
        MyCalendar calendarUnderTest = new MyCalendar(2000,1,1,0,0);

        // ...when the string is returned from the object under test...
        int result = calendarUnderTest.getYear();

        // ...then the result should be the expected one.
        //assertThat(result, is(FAKE_STRING));
        assertEquals(1,result);
    }

}
