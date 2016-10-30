package com.pfp.parkhere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Color;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.android.gms.common.api.GoogleApiClient;

import ObjectClasses.Peer;
import ObjectClasses.Status;

import static com.pfp.parkhere.R.id.editButton;
import static com.pfp.parkhere.R.id.myListedSpacesButton;

public class ProfileActivity extends AppCompatActivity
{

    protected EditText mEmail, mPhone, mFirstName, mLastName;
    private Button mEditButton, mSaveButton, mBookingButton, myListedSpacesButton;
    private ImageView mImageView = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();

        mImageView = (ImageView) findViewById(R.id.imageView);
        mEmail = (EditText) findViewById(R.id.emailLabel);
        mPhone = (EditText) findViewById(R.id.phoneLabel);
        mFirstName = (EditText) findViewById(R.id.firstNameLabel);
        mLastName = (EditText) findViewById(R.id.lastNameLabel);
        mEditButton = (Button) findViewById(editButton);
        mSaveButton = (Button) findViewById(R.id.saveButton);
        mBookingButton = (Button) findViewById(R.id.bookingButton);
        myListedSpacesButton = (Button) findViewById(R.id.myListedSpacesButton);

        //User's own profile
        if(extras == null) {
            disableEditText(mEmail);
            disableEditText(mPhone);
            disableEditText(mFirstName);
            disableEditText(mLastName);

            mEditButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mSaveButton.setVisibility(View.VISIBLE);
                    mEditButton.setVisibility(View.GONE);
                    enableEditText(mPhone);
                    enableEditText(mFirstName);
                    enableEditText(mLastName);
                    enableEditText(mEmail);
                }
            });

            mSaveButton.setVisibility(View.GONE);
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mSaveButton.setVisibility(View.GONE);
                    mEditButton.setVisibility(View.VISIBLE);
                    disableEditText(mPhone);
                    disableEditText(mFirstName);
                    disableEditText(mLastName);
                    disableEditText(mEmail);

                    FirebaseDatabase.getInstance().getReference().child("Peers")
                            .child(Global_ParkHere_Application
                                    .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                            .child("phoneNumber").setValue(mPhone.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Peers")
                            .child(Global_ParkHere_Application
                                    .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                            .child("firstName").setValue(mFirstName.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Peers")
                            .child(Global_ParkHere_Application
                                    .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                            .child("lastName").setValue(mLastName.getText().toString());
                }
            });

            if((Global_ParkHere_Application.getCurrentUserObject().getStatus().equals(Status.OWNER)))
                mBookingButton.setVisibility(View.GONE);
            if((Global_ParkHere_Application.getCurrentUserObject().getStatus().equals(Status.SEEKER)))
                myListedSpacesButton.setVisibility(View.GONE);

            try {
                populateFields();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        String ownerEmail = extras.getString("LISTED_SPACE_OWNEREMAIL");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Checking owner profile
        hideFields();
        FirebaseDatabase.getInstance().getReference().child("Peers")
                .child(Global_ParkHere_Application.reformatEmail(ownerEmail))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Peer owner = dataSnapshot.getValue(Peer.class);
                        mFirstName.setText("Owner first name: " + owner.getFirstName());
                        mEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
                        mEmail.setText("Owner rating: " + owner.getOwnerRating());
                        mPhone.setInputType(InputType.TYPE_CLASS_TEXT);
                        mPhone.setText("Owner review: " + owner.getReview());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void hideFields(){
        mImageView.setVisibility(View.GONE);
        mEditButton.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.GONE);
    }

    private void populateFields() throws InterruptedException {
        Peer currentUser = Global_ParkHere_Application.getCurrentUserObject();
        mImageView.setImageBitmap(currentUser.retrieveDPBitmap());
        mFirstName.setText(currentUser.getFirstName());
        mLastName.setText(currentUser.getLastName());
        mPhone.setText(currentUser.getPhoneNumber());
        mEmail.setText(currentUser.getEmailAddress());

    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
    }

    public void clickedMyBookings(View view)
    {
        startActivity(new Intent(ProfileActivity.this, MyBookingsActivity.class));

    }

    public void clickedMyListedSpaces(View view)
    {
        startActivity(new Intent(ProfileActivity.this, MyListedSpacesActivity.class));

    }

    public void clickedVerification(View view)
    {
        startActivity(new Intent(ProfileActivity.this, VerificationActivity.class));
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
