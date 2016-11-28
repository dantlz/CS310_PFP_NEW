package com.pfp.parkhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Space;
import ObjectClasses.Status;

//DOUBLEUSE
public class PostDetailActivity extends Activity {
    private Button ownerButton, editButton, bookSpaceButton, finishBookingButton;
    private ListView reviewsListView;
    private RatingBar rateBar;

    private String spaceName;
    private String ownerEmail;
    private boolean firstTime;
    private List<String> viewValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        firstTime = true;

        ownerButton = (Button) findViewById(R.id.postdetail_ownerButton);
        editButton = (Button) findViewById(R.id.postdetail_edit_listed_space_button);
        bookSpaceButton = (Button) findViewById(R.id.postdetail_bookSpaceButton);
        finishBookingButton = (Button) findViewById(R.id.postdetail_finishBookingsButton);
        reviewsListView = (ListView) findViewById(R.id.postdetail_listedSpaceReviewListView);
        rateBar = (RatingBar) findViewById(R.id.postdetail_ListedSpacesDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));

        Bundle extras = getIntent().getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        viewValues = new ArrayList<>();

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
                Intent intent = new Intent(PostDetailActivity.this, FinishBookingsActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
                finish();
            }
        });
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, OwnerProfileActivity.class);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, EditListedSpacesActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        bookSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, BookSpaceActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
                finish();
            }
        });


        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Space space = dataSnapshot.getValue(Space.class);
                onCreateContinued(space);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCreateContinued(Space space){
        if(!firstTime){
            return;
        }
        firstTime = false;

        //Came from my listed space detail
        TextView nameText = (TextView) findViewById(R.id.postdetail_listed_space_name);
        nameText.setText(space.getSpaceName());

        TextView addressText = (TextView) findViewById(R.id.postdetail_listed_space_address);
        addressText.setText(space.getStreetAddress());
        addressText.append(",\n" + space.getCity());
        addressText.append(", " + space.getState());
        addressText.append(", " + space.getZipCode());

        TextView priceText = (TextView) findViewById(R.id.postdetail_listed_space_price);
        priceText.setText("$" + space.getPricePerHour());

        TextView typeField = (TextView) findViewById(R.id.postdetail_typeField);
        typeField.setText(String.valueOf(space.getType()));

        TextView policyField = (TextView) findViewById(R.id.postdetail_policyField);
        policyField.setText(String.valueOf(space.getPolicy()));

        TextView descriptionField = (TextView) findViewById(R.id.postdetail_descriptionField);
        descriptionField.setText(space.getDescription());

        rateBar.setRating(space.getSpaceRating());

        Global.spaceReviews().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot entry: dataSnapshot.getChildren()){
                    viewValues.add(entry.getValue(String.class));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        PostDetailActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);

                reviewsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
