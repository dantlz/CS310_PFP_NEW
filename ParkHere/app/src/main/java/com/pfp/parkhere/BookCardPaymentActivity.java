package com.pfp.parkhere;

import android.app.Activity;
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
//Done Sprint 2
public class BookCardPaymentActivity extends Activity {
//TODO Balance needs to change
    private Booking booking;
    private String ownerEmail;
    private String ownerEmailReformatted;
    private String spaceName;
    private String postName;
    private String bookingID;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_card);
        firstTime = true;

        booking = new Booking();

        spaceName = getIntent().getExtras().getString("SPACE_NAME");
        ownerEmail = getIntent().getExtras().getString("SPACE_OWNEREMAIL");
        ownerEmailReformatted = Global.reformatEmail(ownerEmail);
        postName = getIntent().getExtras().getString("POST_NAME");

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
        if(!firstTime){
            return;
        }
        firstTime = false;

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
            AlertDialog.Builder errorDisplay = new AlertDialog.Builder(getBaseContext(), R.style.MyAlertDialogStyle);
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

        //TODO Add unavailable dates to a space after a booking is made.
//        FirebaseDatabase.getInstance().getReference().child("Spaces")
//                .child(Global.reformatEmail(
//                        getIntent().getExtras().getString("OWNEREMAIL")))
//                .child(getIntent().getExtras().getString("SPACENAME"))
    }
}
