package com.pfp.parkhere;

import org.junit.Test;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.PaymentInfo;
import ObjectClasses.Peer;
import ObjectClasses.Space;
import ObjectClasses.SpaceType;
import ObjectClasses.Status;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tshih on 11/7/16.
 */

public class EnumsTest {
    @Test
    public void StatusTest() {
        Peer testPeer = new Peer();
        testPeer.setStatus(Status.BOTH);
        assertEquals(testPeer.getStatus(),Status.BOTH);
    }
    @Test
    public void CancelPolicyTest() {
        Space testSpace = new Space();
        testSpace.setPolicy(CancellationPolicy.LIGHT);
        assertEquals(testSpace.getPolicy(),CancellationPolicy.LIGHT);
    }
    @Test
    public void PaymentInfoTest() {
        Peer testPeer = new Peer();
        testPeer.setPaymentInfo(PaymentInfo.PAYPAL);
        assertEquals(testPeer.getPaymentInfo(),PaymentInfo.PAYPAL);
    }
    @Test
    public void SpaceTypeTest() {
        Space testSpace = new Space();
        testSpace.setType(SpaceType.COMPACT);
        assertEquals(testSpace.getType(),SpaceType.COMPACT);
    }
}
