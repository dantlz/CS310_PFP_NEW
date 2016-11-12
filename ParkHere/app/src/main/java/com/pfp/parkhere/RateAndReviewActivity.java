package com.pfp.parkhere;

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

public class RateAndReviewActivity extends AppCompatActivity {

    private EditText spaceRating, spaceReview, ownerRating, ownerReview;
    private Button finishButton;
    private String ownerEmail, spaceName;
    private int currOwnerRating, currSpaceRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);

        spaceRating = (EditText) findViewById(R.id.spaceRatingField);
        spaceReview = (EditText) findViewById(R.id.spaceReviewField);
        ownerRating = (EditText) findViewById(R.id.ownerRatingField);
        ownerReview = (EditText) findViewById(R.id.ownerReviewField);

        finishButton = (Button) findViewById(R.id.finishRateAndReviewButton);

        Bundle extras = getIntent().getExtras();
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        spaceName = extras.getString("SPACE_NAME");

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRateAndReview();
            }
        });
    }

    private void finishRateAndReview(){
        Global.peers().child(ownerEmail).addValueEventListener(new ValueEventListener() {
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
        Global.spaces().child(ownerEmail).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currSpaceRating = dataSnapshot.getValue(Space.class).getSpaceRating();

                int newOwnerRating = (Integer.valueOf(ownerRating.getText().toString()) + currOwnerRating) / 2; //TODO This definitely should be /2
                int newSpaceRating = (Integer.valueOf(spaceRating.getText().toString()) + currSpaceRating) / 2; //TODO This definitely should be /2

                Global.peers().child(ownerEmail).child("ownerRating").setValue(newOwnerRating);
                Global.spaces().child(ownerEmail).child(spaceName).child("spaceRating").setValue(newSpaceRating);

                Global.ownerReviews().child(ownerEmail).push().setValue(ownerReview.getText().toString());
                Global.spaceReviews().child(ownerEmail).child(spaceName).push().setValue(spaceReview.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
