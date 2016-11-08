package com.pfp.parkhere;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import ObjectClasses.Space;

/**
 * Created by tshih on 11/6/16.
 */

public class SpaceTest {

    @Test
    public void SpaceSetterTest() {
        Space spaceUnderTest = new Space();
        spaceUnderTest.setSpaceRating(3);
        spaceUnderTest.setOwnerEmail("owner@email.net");
        spaceUnderTest.setSpaceName("Space Name");
    }
    @Test
    public void SpaceInvalidDataTest() {

    }

}
