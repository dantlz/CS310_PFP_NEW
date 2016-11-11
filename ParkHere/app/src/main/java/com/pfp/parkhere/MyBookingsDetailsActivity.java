package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyBookingsDetailsActivity extends Activity{
    private String bookingIdentifier, ownerEmail, spaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_details);

        //get information passed from mybookings activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        spaceName = extras.getString("SPACE_NAME");

        TextView addressView = (TextView) findViewById(R.id.streetAddress);
        addressView.setTextColor(Color.BLACK);
        addressView.setText(extras.getString("SPACE_ADDRESS"));

        //set start and end times
        String times = "START: " + extras.getString("SPACE_STARTTIME") + "\n"+ "END: " + extras.getString("SPACE_ENDTIME");
        TextView timesView = (TextView) findViewById(R.id.start_end_time);
        timesView.setTextColor(Color.BLACK);
        timesView.setText(times);

        TextView nameView = (TextView) findViewById(R.id.owner_name);
        nameView.setText(extras.getString("SPACE_OWNERNAME"));
        TextView emailView = (TextView) findViewById(R.id.owner_email);
        emailView.setText(ownerEmail);
        RatingBar rateBar = (RatingBar) findViewById(R.id.BookingDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
        rateBar.setRating(extras.getInt("SPACE_RATING"));
        TextView reviewView = (TextView) findViewById(R.id.space_review);
        String spaceReview = "Review: " + extras.getString("SPACE_REVIEW");
        reviewView.setText(spaceReview);

        bookingIdentifier = extras.getString("BOOKING_IDENTIFIER");
    }

    public void ReturnToBookings(View view){
        finish();
    }

    public void GoToCancel(View view){
        Intent intent = new Intent(this, CancelActivity.class);
        intent.putExtra("BOOKING_IDENTIFIER", bookingIdentifier);
        intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
        intent.putExtra("SPACE_NAME", spaceName);
        startActivity(intent);
    }


}
