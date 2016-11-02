package com.pfp.parkhere;


import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Booking;
import ObjectClasses.Space;

public class PayWithPaypalActivity extends AppCompatActivity {

    private Booking booking;
    private String ownerEmailReformatted;
    private String spaceName;
    private String bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_paypal);

        booking = new Booking();

        spaceName = getIntent().getExtras().getString("SPACE_NAME_IDENTIFIER");
        ownerEmailReformatted = Global.reformatEmail(getIntent().getExtras().getString("OWNER_EMAIL_IDENTIFIER"));
    }


    public void verifyPaypal(View view) {
        Global.spaces().child(ownerEmailReformatted).child(spaceName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Space space = dataSnapshot.getValue(Space.class);
                        completePayment(space);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void completePayment(Space space) {
        booking.setSpaceName(space.getSpaceName());
        booking.setStartCalendarDate(Global.getCurrentSearchTimeDateStart());
        booking.setEndCalendarDate(Global.getCurrentSearchTimedateEnd());
        booking.setBookingSpaceOwnerEmail(space.getOwnerEmail());
        Global.bookings().child(Global.getCurUser().getReformattedEmail()).push().setValue(booking);
        Global.bookings().child(Global.getCurUser().getReformattedEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bookingID = dataSnapshot.getKey();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        Global.spaces().child(ownerEmailReformatted).child(spaceName).child("currentBookingsOwnerEmails").child(ownerEmailReformatted).push().setValue(bookingID);
        startActivity(new Intent(PayWithPaypalActivity.this, MapsActivity.class));
        finish();
        //Maybe add unavailable times to space obejct too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))

    }
}
