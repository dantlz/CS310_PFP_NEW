package com.pfp.parkhere;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Booking;
import ObjectClasses.Space;

public class PayWithCardActivity extends AppCompatActivity {

    private Booking booking;
    private String ownerEmailReformatted;
    private String spaceName;
    private String bookingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_card);

        booking = new Booking();

        spaceName = getIntent().getExtras().getString("SPACE_NAME_IDENTIFIER");
        ownerEmailReformatted = Global.reformatEmail(getIntent().getExtras().getString("OWNER_EMAIL_IDENTIFIER"));

        List<String> cardTypeText = new ArrayList<String>();
        cardTypeText.add("Visa");
        cardTypeText.add("MasterCard");
        cardTypeText.add("American Express");

        ArrayAdapter<String> cardTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cardTypeText);

        Spinner cardSelector = (Spinner)findViewById(R.id.cboose_card_spinner);
        cardSelector.setAdapter(cardTypeAdapter);

    }

    public void finishPayment(View view) {
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

    private void completePayment(Space space){
        String error = "";

        EditText cardNumberField = (EditText)findViewById(R.id.card_number_value);
        EditText cardDateField = (EditText)findViewById(R.id.card_date_value);
        EditText cardNameField = (EditText)findViewById(R.id.card_name_value);
        EditText cardCvField = (EditText)findViewById(R.id.card_cv_value);

        String cardNumber = cardNumberField.getText().toString();
        String cardDate = cardDateField.getText().toString();
        String cardName = cardNameField.getText().toString();
        String cardCV = cardCvField.getText().toString();

        if (cardNumber.length() != 16) {
            error += "Card number should be exactly 16 digits.\n";
        }
        if (cardDate.length() != 4) {
            error += "Card expiration date should be exactly 4 digits.\n";
        }
        if (cardDate.length() != 0) {
            if (cardDate.charAt(0) != '0' && cardDate.charAt(0) != '1') {
                error += "Detected card expiration date is not a valid date. Enter as mmyy.\n";
            }
        }
        if (cardName.length() == 0 || !cardName.contains(" ")) {
            error += "Detected invalid name format. Enter first and last names of the cardholder.\n";
        }
        if (cardCV.length() != 3) {
            error += "Card CV code should be at exactly 3 digits.\n";
        }

        if (!error.equals("")) {
            AlertDialog dialog;
            AlertDialog.Builder errorDisplay = new AlertDialog.Builder(getBaseContext());
            errorDisplay.setTitle("ERROR!")
                    .setMessage(error);
            errorDisplay.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

//            dialog = errorDisplay.create();
//            dialog.show();
        }

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
        startActivity(new Intent(PayWithCardActivity.this, MapsActivity.class));
        finish();
        //Maybe add unavailable times to space object too?
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))
    }
}
