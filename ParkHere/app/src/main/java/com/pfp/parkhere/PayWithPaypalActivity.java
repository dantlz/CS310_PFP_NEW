package com.pfp.parkhere;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Booking;
import ObjectClasses.Space;

public class PayWithPaypalActivity extends AppCompatActivity {

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_paypal);

    }


    public void submitPaypalPayment(View view) {
        booking = new Booking();
        FirebaseDatabase.getInstance().getReference().child("Spaces")
                .child(Global_ParkHere_Application.reformatEmail(
                        getIntent().getExtras().getString("OWNER_EMAIL_IDENTIFIER")))
                .child(getIntent().getExtras().getString("SPACE_NAME_IDENTIFIER"))
                .addValueEventListener(new ValueEventListener() {
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
        booking.setDone(false);
        booking.setStartCalendarDate(Global_ParkHere_Application.getCurrentSearchTimeDateStart());
        booking.setEndCalendarDate(Global_ParkHere_Application.getCurrentSearchTimedateEnd());
        booking.setBookingSpaceOwnerEmail(space.getOwnerEmail());
        FirebaseDatabase.getInstance().getReference().child("Bookings")
                .child(Global_ParkHere_Application.reformatEmail(
                        Global_ParkHere_Application.getCurrentUserObject().getEmailAddress())).push().setValue(booking);

        startActivity(new Intent(PayWithPaypalActivity.this, MapsActivity.class));
        finish();
        //Maybe add unavailable times to space obejct too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global_ParkHere_Application.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))

    }
}
