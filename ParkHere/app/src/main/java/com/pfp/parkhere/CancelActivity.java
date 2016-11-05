package com.pfp.parkhere;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelActivity extends AppCompatActivity {

    private String bookingIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        bookingIdentifier = getIntent().getExtras().getString("BOOKING_IDENTIFIER");
    }

    public void ReturnToBookingDetail(View view) {
        finish();
    }

    public void CancelBooking(View view) {
        //TODO NOWW Take out identifier from space object as well
        Global.bookings().child(Global.getCurUser().getReformattedEmail()).child(bookingIdentifier).removeValue();
        Intent intent = new Intent(CancelActivity.this, MyBookingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
