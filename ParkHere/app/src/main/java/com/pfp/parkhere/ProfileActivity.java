package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Color;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Peer;
import ObjectClasses.Status;

import static com.pfp.parkhere.R.id.editButton;

public class ProfileActivity extends Activity {

    protected EditText mEmail, mPhone, mFirstName, mLastName;
    private Button mEditButton, mSaveButton, mBookingButton, myListedSpacesButton;
    private ImageView mImageView = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    //TODO Allow user to change profile picture
    //TODO If owner. User should see their own reviews as well
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
        if (extras.get("SPACE_OWNEREMAIL") == null) {
            if (extras.get("USER_STATUS").equals("OWNER")) {
                mBookingButton.setVisibility(View.GONE);
                myListedSpacesButton.setVisibility(View.VISIBLE);
            } else {
                myListedSpacesButton.setVisibility(View.GONE);
                mBookingButton.setVisibility(View.VISIBLE);
            }

            RatingBar rateBar = (RatingBar) findViewById(R.id.ProfileRatingBar);
            rateBar.setVisibility(View.GONE);
            if (!Global.getCurUser().getStatus().equals(Status.SEEKER)) {
                rateBar.setVisibility(View.VISIBLE);
                DrawableCompat.setTint(rateBar.getProgressDrawable(), Color.parseColor("#FFCC00"));
                rateBar.setRating(Global.getCurUser().getOwnerRating());
            }

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

                    Global.curUserRef().child("phoneNumber").setValue(mPhone.getText().toString());
                    Global.curUserRef().child("firstName").setValue(mFirstName.getText().toString());
                    Global.curUserRef().child("lastName").setValue(mLastName.getText().toString());
                }
            });

            try {
                populateFields();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

    }

    private void populateFields() throws InterruptedException {
        Peer currentUser = Global.getCurUser();
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
}
