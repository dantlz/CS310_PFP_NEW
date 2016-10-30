package com.pfp.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CancelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
    }
    public void ReturnToBookingDetail(View view){
        finish();
    }
    public void CancelBooking(View view){
        //remove from database. If things work out right, the mybookings activity should reload from database without the deleted booking
        Intent intent = new Intent(this, MyBookingsActivity.class);
        startActivity(intent);
        finish();
    }
}
