package com.pfp.parkhere;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Booking;
import ObjectClasses.Peer;
import ObjectClasses.Space;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tshih on 11/7/16.
 */

public class PeerTest {
    @Test
    public void PeerSetterGetterTest() {
        Peer testPeer = new Peer();
        testPeer.setLastName("Lastname");
        testPeer.setFirstName("Firstname");
        testPeer.setPhoneNumber("1234567890");
        testPeer.setEmailAddress("peer@email.net");
        assertEquals("Lastname",testPeer.getLastName());
        assertEquals("Firstname",testPeer.getFirstName());
        assertEquals("1234567890",testPeer.getPhoneNumber());
        assertEquals("peer@email.net",testPeer.getEmailAddress());

    }
    @Test
    public void SeekerSetterGetterTest() {
        Peer testPeer = new Peer();
        List<Booking> testListedBookings = new ArrayList<Booking>();
        Booking testBooking = new Booking();
        testBooking.setSpaceName("Space Name");
        testListedBookings.add(testBooking);
        testPeer.setCompletedBookings(testListedBookings);
        assertEquals(testPeer.getCompletedBookings().get(0).getSpaceName(),"Space Name");
    }
    @Test
    public void OwnerSetterGetterTest() {
        Peer testPeer = new Peer();
        List<Space> testListedSpaces = new ArrayList<Space>();
        Space testSpace = new Space();
        testSpace.setSpaceName("Space Name");
        testListedSpaces.add(testSpace);
        testPeer.setListedSpaces(testListedSpaces);

        testPeer.setOwnerRating(5);
        testPeer.setReview("Horrible horrible person.");

        assertEquals(testPeer.getListedSpaces().get(0).getSpaceName(),"Space Name");
        assertEquals(testPeer.getOwnerRating(),5);
        assertEquals(testPeer.getReview(),"Horrible horrible person.");
    }
    @Test
    public void OwnerInvalidRatingTest() {
        Peer testPeer = new Peer();
        testPeer.setOwnerRating(-3);
        assertEquals(testPeer.getOwnerRating(),0);
    }
}
