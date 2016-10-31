package com.pfp.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;
import ObjectClasses.Space;

public class PayWithCardActivity extends AppCompatActivity {

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_card);

        List<String> cardTypeText = new ArrayList<String>();
        cardTypeText.add("Visa");
        cardTypeText.add("MasterCard");
        cardTypeText.add("American Express");

        ArrayAdapter<String> cardTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cardTypeText);

        Spinner cardSelector = (Spinner)findViewById(R.id.cboose_card_spinner);
        cardSelector.setAdapter(cardTypeAdapter);

    }

    public void submitCardPayment(View view) {
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

    private void completePayment(Space space){
        booking.setSpaceName(space.getSpaceName());
        booking.setDone(false);
        //TODO Get the start and end date
        booking.setStartCalendarDate(new MyCalendar());
        booking.setEndCalendarDate(new MyCalendar());
        booking.setBookingSpaceOwnerEmail(space.getOwnerEmail());
        FirebaseDatabase.getInstance().getReference().child("Bookings")
                .child(Global_ParkHere_Application.reformatEmail(
                        Global_ParkHere_Application.getCurrentUserObject().getEmailAddress())).push().setValue(booking);

        startActivity(new Intent(PayWithCardActivity.this, MapsActivity.class));
        finish();
        //Maybe add unavailable times to space obejct too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global_ParkHere_Application.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))

    }
}
