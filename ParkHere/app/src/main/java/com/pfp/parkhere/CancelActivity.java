package com.pfp.parkhere;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelActivity extends AppCompatActivity {

    private String bookingIdentifier, ownerEmail, spaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        Bundle extras = getIntent().getExtras();

        bookingIdentifier = extras.getString("BOOKING_IDENTIFIER");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        spaceName = extras.getString("SPACE_NAME");
    }

    public void CancelBooking(View view) {
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("currentBookingIdentifiers").child(bookingIdentifier).removeValue();
        Global.bookings().child(Global.getCurUser().getReformattedEmail()).child(bookingIdentifier).removeValue();
        Intent intent = new Intent(CancelActivity.this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
