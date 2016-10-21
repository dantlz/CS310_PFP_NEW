package com.pfp.parkhere;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    private android.widget.ImageView mImageView; 
    private android.widget.EditText mEmail; 
    private android.widget.EditText mPassword; 
    private android.widget.EditText mPhone; 
    private android.widget.EditText mName; 
    private android.widget.Button mEditButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = (android.widget.ImageView) findViewById(R.id.imageView); 
        mEmail = (android.widget.EditText) findViewById(R.id.emailLabel); 
        mPassword = (android.widget.EditText) findViewById(R.id.passwordLabel); 
        mPhone = (android.widget.EditText) findViewById(R.id.phoneLabel); 
        mName = (android.widget.EditText) findViewById(R.id.nameLabel);

        disableEditText(mEmail); 
        disableEditText(mPassword); 
        disableEditText(mPhone); 
        disableEditText(mName);

        mEditButton = (android.widget.Button) findViewById(R.id.editButton); 
        mEditButton.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View v) { 
                enableEditText(mEmail); 
                enableEditText(mPassword); 
                enableEditText(mPhone); 
                enableEditText(mName); 
            } 
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void disableEditText(android.widget.EditText editText) { 
        editText.setFocusable(false); 
        editText.setEnabled(false); 
        editText.setCursorVisible(false); 
        editText.setBackgroundColor(android.graphics.Color.TRANSPARENT); 
    }  

    private void enableEditText(android.widget.EditText editText) { 
        editText.setFocusable(true); 
        editText.setEnabled(true); 
        editText.setCursorVisible(true); 
    }
}
