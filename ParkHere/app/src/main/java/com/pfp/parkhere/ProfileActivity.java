package com.pfp.parkhere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Color;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.pfp.parkhere.R.id.editButton;

public class ProfileActivity extends AppCompatActivity
{

    protected EditText mEmail, mPassword, mPhone, mName;
    private Button mEditButton, mSaveButton;
    private ImageView mImageView = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = (ImageView)findViewById(R.id.imageView);
        mEmail = (EditText)findViewById(R.id.emailLabel);
        mPassword = (EditText)findViewById(R.id.passwordLabel);
        mPhone = (EditText)findViewById(R.id.phoneLabel);
        mName = (EditText)findViewById(R.id.nameLabel);

        disableEditText(mEmail);
        disableEditText(mPassword);
        disableEditText(mPhone);
        disableEditText(mName);

        mEditButton = (Button) findViewById(editButton);
        mEditButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                mSaveButton.setVisibility(View.VISIBLE);
                enableEditText(mEmail);
                enableEditText(mPassword);
                enableEditText(mPhone);
                enableEditText(mName);
            }
        });

        mSaveButton = (Button) findViewById(R.id.saveButton);
        mSaveButton.setVisibility(View.GONE);
        mSaveButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                mSaveButton.setVisibility(View.GONE);
                disableEditText(mEmail);
                disableEditText(mPassword);
                disableEditText(mPhone);
                disableEditText(mName);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder().setName("Profile Page").setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
                /*
                TODO: Define a title for the content shown.
                TODO: Make sure this auto-generated URL is correct.
                */

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
}
