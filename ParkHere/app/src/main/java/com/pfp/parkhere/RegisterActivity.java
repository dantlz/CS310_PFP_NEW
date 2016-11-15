package com.pfp.parkhere;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Peer;
import ObjectClasses.Status;

public class RegisterActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonLoadImage;
    private Button confirmButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneNumberField;
    private EditText passwordField;
    private EditText repeatPasswordField;
    private Button gotoLoginButton;
    private ImageView imageView;
    private Spinner statusSpinner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Peer currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.registerLoad).setVisibility(View.GONE);

        buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            //Called when Upload Photo is clicked
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButtonPressed();
            }
        });
        firstNameField = (EditText) findViewById(R.id.first_name_field);
        lastNameField = (EditText) findViewById(R.id.last_name_field);
        emailField = (EditText) findViewById(R.id.email_field);
        phoneNumberField = (EditText) findViewById(R.id.phone_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        repeatPasswordField = (EditText) findViewById(R.id.repeat_password_field);
        gotoLoginButton = (Button) findViewById(R.id.goToLoginButton);
        gotoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        imageView = (ImageView) findViewById(R.id.imgView);
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);


        // Spinner Drop down elements
        List<String> types = new ArrayList<String>();
        types.add("Owner");
        types.add("Seeker");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(dataAdapter);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    findViewById(R.id.registerLoad).setVisibility(View.GONE);

                    Global.peers().child(Global.reformatEmail(user.getEmail())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            currentUser = dataSnapshot.getValue(Peer.class);
                            if(currentUser == null)
                                return;
                            Global.setCurUser(currentUser);
                            startActivity(new Intent(RegisterActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                            return;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    return;
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void confirmButtonPressed(){
        findViewById(R.id.registerLoad).setVisibility(View.VISIBLE);

        String validity = allInputFieldValid();
        if(!validity.equals("")) {
            findViewById(R.id.registerLoad).setVisibility(View.GONE);
            new AlertDialog.Builder(RegisterActivity.this, R.style.MyAlertDialogStyle)
                    .setTitle("Registration failed")
                    .setMessage(validity)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailField.getText().toString(),
                passwordField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            findViewById(R.id.registerLoad).setVisibility(View.GONE);
                            //Check if email is already registered.
                            if(task.getException().getClass().equals(FirebaseAuthUserCollisionException.class)){
                                new AlertDialog.Builder(RegisterActivity.this, R.style.MyAlertDialogStyle)
                                        .setTitle("Email already in use")
                                        .setMessage("The email: " + emailField.getText().toString()
                                                + " is already registered. Please register with a different email or log in")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            return;
                        }

                        currentUser = createUserObject();
                        Global.setCurUser(currentUser);
                        Global.peers().child(Global.reformatEmail(emailField.getText().toString())).setValue(currentUser);
                    }
                });
    }

    private Peer createUserObject(){
        Peer peer = new Peer();
        peer.setEmailAddress(emailField.getText().toString());
        peer.setReformattedEmail(Global.reformatEmail(emailField.getText().toString()));
        peer.setFirstName(firstNameField.getText().toString());
        peer.setLastName(lastNameField.getText().toString());
        peer.setPhoneNumber(phoneNumberField.getText().toString());
        peer.setDPNonFirebaseRelated(imageView.getDrawable());
        peer.setStatus(Status.valueOf(statusSpinner.getSelectedItem().toString().toUpperCase()));
        peer.setPreferredStatus(peer.getStatus());
        peer.setPhotoID("");
        peer.setOwnerRating(0);

        return peer;
    }

    private String allInputFieldValid(){

        if(firstNameField.getText().toString().equals(""))
            return "First name cannot be empty";
        if(lastNameField.getText().toString().equals(""))
            return "Last name cannot be empty";
        if(emailField.getText().toString().equals("") ||
                !Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches())
            return "Email cannot be empty and must be valid";
        if(phoneNumberField.getText().toString().equals("") ||
                !Patterns.PHONE.matcher(phoneNumberField.getText().toString()).matches())
            return "Phone number cannot be empty and must be valid";
        if(passwordField.getText().toString().equals("") ||
                repeatPasswordField.getText().toString().equals(""))
            return "Password fields cannot be empty";
        if(!passwordField.getText().toString().equals(repeatPasswordField.getText().toString()))
            return "Password fields must match";
        //Password must be at least 10 characters with upper/lower case, number(s), and special characters
        if(passwordField.getText().toString().length() < 10)
            return "Password must be at least 10 characters";
        if((BitmapDrawable)(imageView.getDrawable()) == null)
            return "Must set a display picture for your profile";
        for(char c: passwordField.getText().toString().toCharArray()){
            if("!@#$%^&*()_+-=".contains(String.valueOf(c)))
                return "";
        }
        return "Password must contain at least one special character";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }


    }
}
