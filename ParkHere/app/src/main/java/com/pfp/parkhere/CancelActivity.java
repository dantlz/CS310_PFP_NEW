package com.pfp.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class CancelActivity extends AppCompatActivity {

    private String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        identifier = getIntent().getExtras().getString("IDENTIFIER");
    }

    public void ReturnToBookingDetail(View view) {
        finish();
    }

    public void CancelBooking(View view) {
        FirebaseDatabase.getInstance().getReference().child("Bookings")
                .child(Global_ParkHere_Application.reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(identifier).removeValue();
        //remove from database. If things work out right, the mybookings activity should reload from database without the deleted booking
        Intent intent = new Intent(CancelActivity.this, MyBookingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
