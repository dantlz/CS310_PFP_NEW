package com.pfp.parkhere;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import ObjectClasses.MyCalendar;

/**
 * Created by tshih on 11/6/16.
 */

public class MyCalendarTest {

    @Test
    public void myCalendarConstructorTest() {
        // near Y2k
        MyCalendar calendarUnderTest = new MyCalendar(2000,1,2,4,30);

        // ...when the string is returned from the object under test...
        int resultMonth = calendarUnderTest.getMonth();
        int resultYear = calendarUnderTest.getYear();
        int resultDay = calendarUnderTest.getDay();
        int resultHour = calendarUnderTest.getHour();
        int resultMin = calendarUnderTest.getMinute();

        // ...then the result should be the expected one.
        //assertThat(result, is(FAKE_STRING));
        assertEquals(1,resultMonth);
        assertEquals(2000,resultYear);
        assertEquals(2,resultDay);
        assertEquals(4,resultHour);
        assertEquals(30,resultMin);
    }
    @Test
    public void myCalendarInvalidConstructorTest() {
        // near Y2k
        MyCalendar calendarUnderTest = new MyCalendar(-2000,13,300, 27,700);

        // ...when the string is returned from the object under test...
        int resultMonth = calendarUnderTest.getMonth();
        int resultYear = calendarUnderTest.getYear();
        int resultDay = calendarUnderTest.getDay();
        int resultHour = calendarUnderTest.getHour();
        int resultMin = calendarUnderTest.getMinute();

        // ...then the result should be the expected one.
        //assertThat(result, is(FAKE_STRING));
        assertEquals(0,resultMonth);
        assertEquals(-2000,resultYear);
        assertEquals(0,resultDay);
        assertEquals(0,resultHour);
        assertEquals(0,resultMin);
    }

}
