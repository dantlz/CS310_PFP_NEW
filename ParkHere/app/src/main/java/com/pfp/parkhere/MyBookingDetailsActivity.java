package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;
//Done Sprint 2
public class MyBookingDetailsActivity extends Activity{
    private String bookingIdentifier, ownerEmail, spaceName, spaceAddress, startTime, endTime, ownerName, postName;
    private TextView addressView, timesView, nameView, emailView;
    private Button rateAndReviewButton, cancelButton;
    private RatingBar rateBar;
    private ListView reviewsView;
    private int spaceRating;
    private boolean done;

    private Vector<String> viewValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_details);

        viewValues = new Vector<>();

        //Get info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        spaceName = extras.getString("SPACE_NAME");
        postName = extras.getString("POST_NAME");
        bookingIdentifier = extras.getString("BOOKING_IDENTIFIER");
        spaceAddress = extras.getString("SPACE_ADDRESS");
        startTime = extras.getString("BOOKING_STARTTIME");
        endTime = extras.getString("BOOKING_ENDTIME");
        ownerName = extras.getString("SPACE_OWNERNAME");
        spaceRating = extras.getInt("SPACE_RATING");
        done = extras.getBoolean("BOOKING_DONE");

        //Get reference to Ui elements
        addressView = (TextView) findViewById(R.id.streetAddress);
        timesView = (TextView) findViewById(R.id.start_end_time);
        nameView = (TextView) findViewById(R.id.owner_name);
        emailView = (TextView) findViewById(R.id.owner_email);
        rateBar = (RatingBar) findViewById(R.id.SpaceAverageRating);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
        reviewsView = (ListView) findViewById(R.id.space_reviews);
        rateAndReviewButton = (Button) findViewById(R.id.Rate_Review_BookDetails);
        cancelButton = (Button) findViewById(R.id.Booking_To_Cancel);
//TODO cancelButton doesn't actually do anything right now
        if(done)
            cancelButton.setVisibility(View.GONE);
        else
            rateAndReviewButton.setVisibility(View.GONE);

        addressView.setText(spaceAddress);
        timesView.setText("START: " + startTime + "\n"+ "END: " + endTime);
        nameView.setText(ownerName);
        emailView.setText(ownerEmail);
        rateBar.setRating(spaceRating);

        Global.spaceReviews().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot entry: dataSnapshot.getChildren()){
                    viewValues.add(entry.getValue(String.class));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MyBookingDetailsActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);

                reviewsView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void ReturnToBookings(View view){
        finish();
    }

    public void GoToCancel(View view){
        Intent intent = new Intent(this, CancelBookingActivity.class);
        intent.putExtra("BOOKING_IDENTIFIER", bookingIdentifier);
        intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
        intent.putExtra("SPACE_NAME", spaceName);
        intent.putExtra("POST_NAME", postName);
        startActivity(intent);
        finish();
    }
    //This function opens the Owner details of this space
    public void GoToOwnerDetail(View view){
        Intent intent = new Intent(this, ProfileOwnerActivity.class);
        intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
        startActivity(intent);
        finish();
    }

    public void GoToRateAndReview(View view){
        Intent intent = new Intent(this, RateAndReviewActivity.class);
        intent.putExtra("BOOKING_IDENTIFIER", bookingIdentifier);
        intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
        intent.putExtra("SPACE_NAME", spaceName);
        intent.putExtra("POST_NAME", postName);
        startActivity(intent);
        finish();
    }
}
