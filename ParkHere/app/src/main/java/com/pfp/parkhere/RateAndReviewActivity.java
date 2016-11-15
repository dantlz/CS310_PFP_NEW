package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Peer;
import ObjectClasses.Space;

public class RateAndReviewActivity extends Activity {

    private EditText spaceRating, spaceReview, ownerRating, ownerReview;
    private Button finishButton;
    private String ownerEmail, spaceName, bookingIdentifier;
    private int currOwnerRating, currSpaceRating;
    private boolean firstTime;

    //TODO Add limit between 1-5 for rating
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);
        firstTime = true;

        spaceRating = (EditText) findViewById(R.id.spaceRatingField);
        spaceReview = (EditText) findViewById(R.id.spaceReviewField);
        ownerRating = (EditText) findViewById(R.id.ownerRatingField);
        ownerReview = (EditText) findViewById(R.id.ownerReviewField);

        finishButton = (Button) findViewById(R.id.finishRateAndReviewButton);

        Bundle extras = getIntent().getExtras();
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        spaceName = extras.getString("SPACE_NAME");
        bookingIdentifier = extras.getString("BOOKING_IDENTIFIER");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRateAndReview();
            }
        });
    }

    private void finishRateAndReview(){
        Global.peers().child(Global.reformatEmail(ownerEmail)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currOwnerRating = dataSnapshot.getValue(Peer.class).getOwnerRating();
                getSpaceRating();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void getSpaceRating(){
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currSpaceRating = dataSnapshot.getValue(Space.class).getSpaceRating();

                if(!firstTime)
                    return;
                firstTime = false;

                int newOwnerRating = (Integer.valueOf(ownerRating.getText().toString()) + currOwnerRating) / 2; //TODO This definitely should be /2
                int newSpaceRating = (Integer.valueOf(spaceRating.getText().toString()) + currSpaceRating) / 2; //TODO This definitely should be /2

                Global.peers().child(Global.reformatEmail(ownerEmail)).child("ownerRating").setValue(newOwnerRating);
                Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("spaceRating").setValue(newSpaceRating);

                Global.ownerReviews().child(Global.reformatEmail(ownerEmail)).push().setValue(ownerReview.getText().toString());
                Global.spaceReviews().child(Global.reformatEmail(ownerEmail)).child(spaceName).push().setValue(spaceReview.getText().toString());
                Global.bookings().child(Global.getCurUser().getReformattedEmail()).child(bookingIdentifier).removeValue();
                startActivity(new Intent(RateAndReviewActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
