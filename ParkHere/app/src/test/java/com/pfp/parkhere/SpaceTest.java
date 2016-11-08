package com.pfp.parkhere;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ObjectClasses.MyCalendar;
import ObjectClasses.Space;

/**
 * Created by tshih on 11/6/16.
 */

public class SpaceTest {

    @Test
    public void SpaceSetterGetterStringTest() {
        Space spaceUnderTest = new Space();
        spaceUnderTest.setOwnerEmail("owner@email.net");
        spaceUnderTest.setSpaceName("Space Name");
        spaceUnderTest.setSpaceReview("BAD");
        spaceUnderTest.setDescription("The driveway");
        spaceUnderTest.setCity("Los Angeles");
        spaceUnderTest.setStreetAddress("3025 Royal St.");
        spaceUnderTest.setState("California");
        spaceUnderTest.setZipCode("90007");
        assertEquals("owner@email.net", spaceUnderTest.getOwnerEmail());
        assertEquals("Space Name", spaceUnderTest.getSpaceName());
        assertEquals("BAD",spaceUnderTest.getSpaceReview());
        assertEquals("The driveway",spaceUnderTest.getDescription());
        assertEquals("Los Angeles",spaceUnderTest.getCity());
        assertEquals("3025 Royal St.",spaceUnderTest.getStreetAddress());
        assertEquals("California",spaceUnderTest.getState());
        assertEquals("90007",spaceUnderTest.getZipCode());
    }
    @Test
    public void SpaceSetterGetterNonStringTest() {
        Space spaceUnderTest = new Space();
        spaceUnderTest.setSpaceRating(3);
        spaceUnderTest.setPricePerHour(3);
        spaceUnderTest.setAvailableStartDateAndTime(new MyCalendar(2000,1,2,3,4));
        spaceUnderTest.setAvailableEndDateAndTime(new MyCalendar(2000,1,2,3,15));
        assertEquals(3,spaceUnderTest.getSpaceRating());
        assertEquals(3,spaceUnderTest.getPricePerHour());
        assertEquals(2000,spaceUnderTest.getAvailableStartDateAndTime().getYear());
        assertEquals(2000,spaceUnderTest.getAvailableEndDateAndTime().getYear());
        assertEquals(1,spaceUnderTest.getAvailableStartDateAndTime().getMonth());
        assertEquals(1,spaceUnderTest.getAvailableEndDateAndTime().getMonth());
        assertEquals(2,spaceUnderTest.getAvailableStartDateAndTime().getDay());
        assertEquals(2,spaceUnderTest.getAvailableEndDateAndTime().getDay());
        assertEquals(3,spaceUnderTest.getAvailableStartDateAndTime().getHour());
        assertEquals(3,spaceUnderTest.getAvailableEndDateAndTime().getHour());
        assertEquals(4,spaceUnderTest.getAvailableStartDateAndTime().getMinute());
        assertEquals(15,spaceUnderTest.getAvailableEndDateAndTime().getMinute());
    }
    @Test
    public void SpaceInvalidDatesTest() {
        Space spaceUnderTest = new Space();
        spaceUnderTest.setAvailableStartDateAndTime(new MyCalendar(2000,1,2,3,40));
        spaceUnderTest.setAvailableEndDateAndTime(new MyCalendar(2000,1,2,3,38));
        //makes sure the start year is less than or equal to the end year
        int startDateTemp = 366*spaceUnderTest.getAvailableStartDateAndTime().getYear() +
                31* spaceUnderTest.getAvailableStartDateAndTime().getMonth() +
                spaceUnderTest.getAvailableStartDateAndTime().getDay();
        //weighs years as 366, months as 31, and days
        int endDateTemp = 366*spaceUnderTest.getAvailableEndDateAndTime().getYear() +
                31* spaceUnderTest.getAvailableEndDateAndTime().getMonth() +
                spaceUnderTest.getAvailableEndDateAndTime().getDay();
        if(startDateTemp > endDateTemp){
            fail("Start date is later than end date");
        }
        else if(startDateTemp == endDateTemp){
            int startTime = 60*spaceUnderTest.getAvailableStartDateAndTime().getHour()+
                    spaceUnderTest.getAvailableStartDateAndTime().getMinute();
            int endTime = 60*spaceUnderTest.getAvailableEndDateAndTime().getHour()+
                    spaceUnderTest.getAvailableEndDateAndTime().getMinute();
            if(startTime > endTime){
                fail("Start time is later than end time");
            }
        }
        assertTrue(true);
    }

}
