package com.pfp.parkhere;

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

//DOUBLEUSE
public class MyListedSpacesDetailsActivity extends AppCompatActivity {

    private Bundle extras;
    private RatingBar rateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces_details);

        Intent intent = getIntent();
        extras = intent.getExtras();

        Button ownerButton = (Button) findViewById(R.id.ownerButton);
        Button editButton = (Button) findViewById(R.id.edit_listed_space_button);
        Button bookSpaceButton = (Button) findViewById(R.id.bookSpaceButton);
        Button confirmBookingButton = (Button) findViewById(R.id.finishBookingsButton);

        confirmBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, FinishBookingsActivity.class);

                startActivity(intent);
            }
        });

        rateBar = (RatingBar) findViewById(R.id.ListedSpacesDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));

        //My listed space detail
        if(extras.getString("SPACENAME") == null) {
            TextView nameText = (TextView) findViewById(R.id.listed_space_name);
            nameText.setText(extras.getString("LISTED_SPACE_NAME"));

            TextView addressText = (TextView) findViewById(R.id.listed_space_address);
            addressText.setText(extras.getString("LISTED_SPACE_ADDRESS"));
            addressText.append(",\n" + extras.getString("LISTED_SPACE_CITY"));
            addressText.append(", " + extras.getString("LISTED_SPACE_STATE"));
            addressText.append(", " + extras.getString("LISTED_SPACE_ZIP"));

            TextView priceText = (TextView) findViewById(R.id.listed_space_price);
            priceText.setText("$" + extras.getInt("LISTED_SPACE_PRICE"));

            TextView typeField = (TextView) findViewById(R.id.typeField);
            typeField.setText(extras.getString("LISTED_SPACE_TYPE"));

            TextView policyField = (TextView) findViewById(R.id.policyField);
            policyField.setText(extras.getString("LISTED_SPACE_POLICY"));

            TextView descriptionField = (TextView) findViewById(R.id.descriptionField);
            descriptionField.setText(extras.getString("LISTED_SPACE_DESCRIPTION"));

            rateBar.setRating(extras.getInt("LISTED_SPACE_RATING"));

            TextView spaceReviewField = (TextView) findViewById(R.id.spaceReviewField);
            spaceReviewField.setText(extras.getString("LISTED_SPACE_REVIEW"));
            ownerButton.setVisibility(View.GONE);
            bookSpaceButton.setVisibility(View.GONE);

            return;
        }

        if(extras.getString("OWNEREMAIL").equals(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress())){
            bookSpaceButton.setVisibility(View.GONE);
        }
        //Directed from map or resultList
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, ProfileActivity.class);
                intent.putExtra("LISTED_SPACE_OWNEREMAIL", extras.getString("OWNEREMAIL"));
                startActivity(intent);
            }
        });
        editButton.setVisibility(View.GONE);
        if(extras.getString("STATUS").equals("OWNER")){
            bookSpaceButton.setVisibility(View.GONE);
        }
        bookSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, BookSpaceActivity.class);
                intent.putExtra("SPACENAME", extras.getString("SPACENAME"));
                intent.putExtra("OWNEREMAIL", extras.getString("OWNEREMAIL"));
                startActivity(intent);
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Spaces")
                .child(Global_ParkHere_Application.reformatEmail(extras.getString("OWNEREMAIL")))
                .child(extras.getString("SPACENAME")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateNotMySpaceDetails(dataSnapshot.getValue(Space.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateNotMySpaceDetails(Space space){
        TextView nameText = (TextView) findViewById(R.id.listed_space_name);
        nameText.setText(space.getSpaceName());

        TextView addressText = (TextView) findViewById(R.id.listed_space_address);
        addressText.setText(space.getStreetAddress());
        addressText.append(",\n" + space.getCity());
        addressText.append(", " + space.getState());
        addressText.append(", " + space.getState());

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

    public void onEditListedSpaceClicked(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, EditListedSpacesActivity.class);

        intent.putExtras(extras);
        context.startActivity(intent);
    }


}
