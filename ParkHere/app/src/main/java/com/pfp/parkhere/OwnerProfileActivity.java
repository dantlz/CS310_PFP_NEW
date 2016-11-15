package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Peer;
import ObjectClasses.Space;
public class OwnerProfileActivity extends Activity {

    private String ownerEmail;
    private TextView mFirstName;
    private RatingBar ratingBar;
    private ListView reviewsListView;
    private List<String> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        ownerEmail = getIntent().getExtras().getString("SPACE_OWNEREMAIL");
        reviews = new ArrayList<>();

        mFirstName = (TextView) findViewById(R.id.ownerProfileFirstName);
        ratingBar = (RatingBar) findViewById(R.id.ownerProfileRatingBar);
        reviewsListView = (ListView) findViewById(R.id.ownerProfileReviews);

        //Checking owner profile
        Global.peers().child(Global.reformatEmail(ownerEmail)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Peer owner = dataSnapshot.getValue(Peer.class);

                mFirstName.setText(owner.getFirstName());
                DrawableCompat.setTint(ratingBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
                ratingBar.setRating(owner.getOwnerRating());

                Global.ownerReviews().child(Global.reformatEmail(ownerEmail)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            reviews.add(snapshot.getValue(String.class));
                        }
                        populateReviews();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateReviews(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, reviews);

        reviewsListView.setAdapter(arrayAdapter);
    }
}
