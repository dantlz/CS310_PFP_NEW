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

//TODO Have the balance in both peers change upon completion. Calculations done by hour and hourly rate
public class PayWithPaypalActivity extends Activity {

    private Booking booking;
    private String ownerEmail, ownerEmailReformatted, spaceName, bookingID;
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

        booking.setSpaceName(space.getSpaceName());
        booking.setStartCalendarDate(Global.getCurrentSearchTimeDateStart());
        booking.setEndCalendarDate(Global.getCurrentSearchTimedateEnd());
        booking.setBookingSpaceOwnerEmail(space.getOwnerEmail());
        booking.setDone(false);
        DatabaseReference addedBookingRef = Global.bookings().child(Global.getCurUser().getReformattedEmail()).push();
        addedBookingRef.setValue(booking);

        bookingID = addedBookingRef.getKey();
        Global.spaces().child(ownerEmailReformatted).child(spaceName).child("currentBookingIdentifiers").child(bookingID).setValue(Global.getCurUser().getEmailAddress());
        startActivity(new Intent(PayWithPaypalActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();

        //TODO Maybe add unavailable times to space object too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))

    }
}
