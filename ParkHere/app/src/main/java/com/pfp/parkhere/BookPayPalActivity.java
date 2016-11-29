package com.pfp.parkhere;


import android.app.Activity;
import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Booking;
import ObjectClasses.Space;
//Done Sprint 2
//TODO Have the balance in both peers change upon completion. Calculations done by hour and hourly rate
//TODO Merge pay with paypal and car. Don't need 2 actiities
public class BookPayPalActivity extends Activity {

    private Booking booking;
    private String ownerEmail, ownerEmailReformatted, spaceName, bookingID, postName;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_paypal);
        firstTime = true;

        booking = new Booking();

        spaceName = getIntent().getExtras().getString("SPACE_NAME");
        ownerEmail = getIntent().getExtras().getString("SPACE_OWNEREMAIL");
        ownerEmailReformatted = Global.reformatEmail(ownerEmail);
        postName = getIntent().getExtras().getString("POST_NAME");
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
        if(!firstTime){
            return;
        }
        firstTime = false;

        booking.setPostName(postName);
        booking.setSpaceName(space.getSpaceName());
        booking.setStartCalendarDate(Global.getCurrentSearchTimeDateStart());
        booking.setEndCalendarDate(Global.getCurrentSearchTimedateEnd());
        booking.setBookingSpaceOwnerEmail(space.getOwnerEmail());
        booking.setDone(false);
        DatabaseReference addedBookingRef = Global.bookings().child(Global.getCurUser().getReformattedEmail()).push();
        addedBookingRef.setValue(booking);

        bookingID = addedBookingRef.getKey();
        Global.posts().child(ownerEmailReformatted).child(spaceName).child(postName).child("currentBookingIdentifiers").child(bookingID).setValue(Global.getCurUser().getEmailAddress());
        finish();

        //TODO Maybe add unavailable times to space object too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))

    }
}
