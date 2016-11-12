package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyBookingsDetailsActivity extends Activity{
    private String bookingIdentifier, ownerEmail, spaceName;
    private String[] viewValues;
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
        addressView.setText(extras.getString("SPACE_ADDRESS"));

        //set start and end times TODO SPRINT Start date end date for booking are null
        String times = "START: " + extras.getString("SPACE_STARTTIME") + "\n"+ "END: " + extras.getString("SPACE_ENDTIME");
        TextView timesView = (TextView) findViewById(R.id.start_end_time);
        timesView.setText(times);

        TextView nameView = (TextView) findViewById(R.id.owner_name);
        nameView.setText(extras.getString("SPACE_OWNERNAME"));
        TextView emailView = (TextView) findViewById(R.id.owner_email);
        emailView.setText(ownerEmail);
        RatingBar rateBar = (RatingBar) findViewById(R.id.SpaceAverageRating);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
        rateBar.setRating(extras.getInt("SPACE_RATING"));
        //String spaceReview = "Review: " + extras.getString("SPACE_REVIEW");
        //reviewView.setText(spaceReview);
        mockReviews();
        //populateReviews();
        //Determines whether to show "Rate and Review" button
        boolean bookingDone = true;
        if(bookingDone){
            View rate_Review_Button = findViewById(R.id.Rate_Review_BookDetails);
            rate_Review_Button.setVisibility(View.VISIBLE);
        }
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
    //This function opens the Owner details of this space
    public void GoToOwnerDetail(View view){
        //ownerEmail;
        //Intent intent = new Intent(this, Activity.class);
        //startActivity(intent);
    }
    //Creates Mock Reviews for app
    public void mockReviews(){
        ListView reviewsView = (ListView) findViewById(R.id.space_reviews);
        viewValues = new String [11];
        for(int i = 0; i < 11;i++){
            viewValues[i] = "Review # " + i + " Blahblahblah car got towed away.";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);
        // Assign adapter to ListView
        reviewsView.setAdapter(adapter);
    }
    //Used in actual app
//    public void populateReviews(){
//    ListView reviewsView = (ListView) findViewById(R.id.space_reviews);
//        viewValues = new String [sizeOfReviewList];
//        for(int i = 0; i < sizeOfReviewList;i++){
//            viewValues[i] = reviewList[i];
//        }
//    }
    public void GoToRateAndReview(View view){
        //Intent intent = new Intent(this, Activity.class);
        //startActivity(intent);
    }
}
