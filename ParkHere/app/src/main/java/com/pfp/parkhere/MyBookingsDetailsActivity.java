package com.pfp.parkhere;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by tshih on 10/21/16.
 */

public class MyBookingsDetailsActivity extends AppCompatActivity{
    String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_details);

        //get information passed from mybookings activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        TextView addressView = (TextView) findViewById(R.id.streetAddress);
        addressView.setTextColor(Color.BLACK);
        addressView.setText(extras.getString("ADDRESS_TEXT"));

        //set start and end times
        String times = "START: " + extras.getString("START_TIME_TEXT") + "\n"+ "END: " + extras.getString("END_TIME_TEXT");
        TextView timesView = (TextView) findViewById(R.id.start_end_time);
        timesView.setTextColor(Color.BLACK);
        timesView.setText(times);

        TextView nameView = (TextView) findViewById(R.id.owner_name);
        nameView.setText(extras.getString("OWNER_NAME_TEXT"));
        TextView emailView = (TextView) findViewById(R.id.owner_email);
        emailView.setText(extras.getString("OWNER_EMAIL_TEXT"));
        TextView ratingView = (TextView) findViewById(R.id.space_rating);
        String rating = "Rating: " + extras.getInt("SPACE_RATING_INT");
        RatingBar rateBar = (RatingBar) findViewById(R.id.BookingDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
        rateBar.setRating(extras.getInt("SPACE_RATING_INT"));
        ratingView.setText(rating);
        TextView reviewView = (TextView) findViewById(R.id.space_review);
        String spaceReview = "Review: " + extras.getString("SPACE_REVIEW_TEXT");
        reviewView.setText(spaceReview);

        identifier = extras.getString("IDENTIFIER");
    }

    public void ReturnToBookings(View view){
        finish();
    }

    public void GoToCancel(View view){
        Intent intent = new Intent(this, CancelActivity.class);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().signOut();
        super.onStop();
    }
}
