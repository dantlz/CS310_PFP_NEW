package com.pfp.parkhere;

import org.junit.Test;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by tshih on 11/7/16.
 */

public class BookingTest {
    @Test
    public void BookingSetterGetterTest() {
        Booking testBooking = new Booking();
        testBooking.setSpaceName("Space Name");
        testBooking.setStartCalendarDate(new MyCalendar(2000,1,2,3,40));
        testBooking.setEndCalendarDate(new MyCalendar(2000,1,2,3,42));
        testBooking.setBookingSpaceOwnerEmail("owner@email.net");
        assertEquals("Space Name",testBooking.getSpaceName());
        assertEquals("owner@email.net",testBooking.getBookingSpaceOwnerEmail());
        assertEquals(2000,testBooking.getStartCalendarDate().getYear());
        assertEquals(2000,testBooking.getEndCalendarDate().getYear());
        assertEquals(1,testBooking.getStartCalendarDate().getMonth());
        assertEquals(1,testBooking.getEndCalendarDate().getMonth());
        assertEquals(2,testBooking.getStartCalendarDate().getDay());
        assertEquals(2,testBooking.getEndCalendarDate().getDay());
        assertEquals(3,testBooking.getStartCalendarDate().getHour());
        assertEquals(3,testBooking.getEndCalendarDate().getHour());
        assertEquals(40,testBooking.getStartCalendarDate().getMinute());
        assertEquals(42,testBooking.getEndCalendarDate().getMinute());
    }
    @Test
    public void BookingInvalidDatesTest() {
        Booking testBooking = new Booking();
        testBooking.setStartCalendarDate(new MyCalendar(2000,1,2,3,40));
        testBooking.setEndCalendarDate(new MyCalendar(2000,1,2,3,38));
        //makes sure the start year is less than or equal to the end year
        int startDateTemp = 366*testBooking.getStartCalendarDate().getYear() +
                31* testBooking.getStartCalendarDate().getMonth() +
                testBooking.getStartCalendarDate().getDay();
        //weighs years as 366, months as 31, and days
        int endDateTemp = 366*testBooking.getEndCalendarDate().getYear() +
                31* testBooking.getEndCalendarDate().getMonth() +
                testBooking.getEndCalendarDate().getDay();
        if(startDateTemp > endDateTemp){
            fail("Start date is later than end date");
        }
        else if(startDateTemp == endDateTemp){
            int startTime = 60*testBooking.getStartCalendarDate().getHour()+
                    testBooking.getStartCalendarDate().getMinute();
            int endTime = 60*testBooking.getEndCalendarDate().getHour()+
                    testBooking.getEndCalendarDate().getMinute();
            if(startTime > endTime){
                fail("Start time is later than end time");
            }
        }
        assertTrue(true);
    }
}
