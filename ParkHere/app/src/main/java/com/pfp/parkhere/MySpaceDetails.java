package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Space;
import ObjectClasses.Status;

//DOUBLEUSE
//Done Sprint 2
//TODO Fix the ListView inside a ScrollView issue in MyBookingDetailsActivity

public class MySpaceDetails extends Activity {
    private Button ownerButton, editButton, addPostButton;
    private ListView reviewsListView, postsListView;
    private RatingBar rateBar;

    private String spaceName;
    private String ownerEmail;
    private boolean firstTime;
    private List<String> reviewListViewValues, postsListViewValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces_details);
        firstTime = true;

        ownerButton = (Button) findViewById(R.id.ownerButton);
        editButton = (Button) findViewById(R.id.edit_listed_space_button);
        addPostButton = (Button) findViewById(R.id.addPostButton);
        reviewsListView = (ListView) findViewById(R.id.listedSpaceReviewListView);
        postsListView = (ListView) findViewById(R.id.postsListView);
        rateBar = (RatingBar) findViewById(R.id.ListedSpacesDetailRatingBar);
        DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));

        Bundle extras = getIntent().getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        reviewListViewValues = new ArrayList<>();

        //Can't book my own space or look at myself, but can confirm/edit space
        if(ownerEmail.equals(Global.getCurUser().getEmailAddress())){
            ownerButton.setVisibility(View.GONE);
        }
        //Can book or look at someone else, but can't modify their stuff
        else{
            editButton.setVisibility(View.GONE);
            addPostButton.setVisibility(View.GONE);
        }

        //Owners can't book space, but can view other owners, confirm/edit his own
        if(Global.getCurUser().getPreferredStatus().equals(Status.OWNER)){
        }
        //Seekers can book space or look at owner, but can't confirm or edit.
        else{
            editButton.setVisibility(View.GONE);
            addPostButton.setVisibility(View.GONE);
        }

        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySpaceDetails.this, ProfileOwnerActivity.class);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySpaceDetails.this, EditSpaceActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySpaceDetails.this, AddPostActivity.class);
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

        TextView descriptionField = (TextView) findViewById(R.id.descriptionField);
        descriptionField.setText(space.getDescription());

        rateBar.setRating(space.getSpaceRating());

        Global.spaceReviews().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot entry: dataSnapshot.getChildren()){
                    reviewListViewValues.add(entry.getValue(String.class));
                }

                //Reviews List
                ArrayAdapter<String> reviewsAdapter = new ArrayAdapter<>(
                        MySpaceDetails.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, reviewListViewValues);
                reviewsListView.setAdapter(reviewsAdapter);

                getPosts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPosts(){
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("PostNames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot entry: dataSnapshot.getChildren()){
                    postsListViewValues.add(entry.getValue(String.class));
                }

                //Posts List
                final ArrayAdapter<String> postsAdapter = new ArrayAdapter<>(
                        MySpaceDetails.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, postsListViewValues);
                postsListView.setAdapter(postsAdapter);
                postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MySpaceDetails.this, MyPostDetailsActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("SPACE_OWNEREMAIL", ownerEmail);
                        extras.putString("SPACE_NAME", spaceName);
                        extras.putString("POST_NAME", postsListViewValues.get(position));
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
