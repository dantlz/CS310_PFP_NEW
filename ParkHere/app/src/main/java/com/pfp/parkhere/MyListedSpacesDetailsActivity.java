package com.pfp.parkhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Space;
import ObjectClasses.Status;
//TODO SPRINT Make review into listview. Get reviews from spaceReviews
//DOUBLEUSE
public class MyListedSpacesDetailsActivity extends Activity {

    private Bundle extras;
    private RatingBar rateBar;

    private String spaceName;
    private String ownerEmail;

    private Button ownerButton, editButton, bookSpaceButton, finishBookingButton;

    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces_details);
        firstTime = true;

        ownerButton = (Button) findViewById(R.id.ownerButton);
        editButton = (Button) findViewById(R.id.edit_listed_space_button);
        bookSpaceButton = (Button) findViewById(R.id.bookSpaceButton);
        finishBookingButton = (Button) findViewById(R.id.finishBookingsButton);

        rateBar = (RatingBar) findViewById(R.id.ListedSpacesDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));

        Intent intent = getIntent();
        extras = intent.getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");

        //Can't book my own space or look at myself, but can confirm/edit space
        if(ownerEmail.equals(Global.getCurUser().getEmailAddress())){
            bookSpaceButton.setVisibility(View.GONE);
            ownerButton.setVisibility(View.GONE);
        }
        //Can book or look at someone else, but can't modify their stuff
        else{
            finishBookingButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        //Owners can't book space, but can view other owners, confirm/edit his own
        if(Global.getCurUser().getPreferredStatus().equals(Status.OWNER)){
            bookSpaceButton.setVisibility(View.GONE);
        }
        //Seekers can book space or look at owner, but can't confirm or edit.
        else{
            finishBookingButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        finishBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, FinishBookingsActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
            }
        });
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, OwnerProfileActivity.class);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, EditListedSpacesActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return;
            }
        });
        bookSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, BookSpaceActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
                finish();
            }
        });

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Space space = dataSnapshot.getValue(Space.class);
                onCreateContinued(space);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(listener);
    }

    private void onCreateContinued(Space space){
        if(!firstTime){
            return;
        }
        firstTime = false;

        //Came from my listed space detail
        TextView nameText = (TextView) findViewById(R.id.listed_space_name);
        nameText.setText(space.getSpaceName());

        TextView addressText = (TextView) findViewById(R.id.listed_space_address);
        addressText.setText(space.getStreetAddress());
        addressText.append(",\n" + space.getCity());
        addressText.append(", " + space.getState());
        addressText.append(", " + space.getZipCode());

        TextView priceText = (TextView) findViewById(R.id.listed_space_price);
        priceText.setText("$" + space.getPricePerHour());

        TextView typeField = (TextView) findViewById(R.id.typeField);
        typeField.setText(String.valueOf(space.getType()));

        TextView policyField = (TextView) findViewById(R.id.policyField);
        policyField.setText(String.valueOf(space.getPolicy()));

        TextView descriptionField = (TextView) findViewById(R.id.descriptionField);
        descriptionField.setText(space.getDescription());

        rateBar.setRating(space.getSpaceRating());

        TextView spaceReviewField = (TextView) findViewById(R.id.spaceReviewField);
        spaceReviewField.setText(space.getSpaceReview());

    }

}
