package com.pfp.parkhere;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import static android.R.attr.data;

public class RegisterActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonLoadImage;
    private Button confirmButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText phoneNumberField;
    private EditText passwordField;

    final String expression = "[(?=.{10, 20})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])]";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // from http://viralpatel.net/blogs/pick-image-from-galary-android-app/
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

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void confirmButtonPressed(){
        if(!allInputFieldValid()) {
            //Let the user know
            return;
        }

//        mAuth.createUserWithEmailAndPassword(emailField.getText().toString(),
//                passwordField.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(getActivity(), (String)data.result,
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        Toast.makeText(getActivity(), (String)data.result,
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
    }

    private boolean allInputFieldValid(){

        if(firstNameField.getText().toString() == null)
            return false;
        if(lastNameField.getText().toString() == null)
            return false;
        if(emailField.getText().toString() == null ||
                !Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches())
            return false;
        if(phoneNumberField.getText().toString() == null ||
                !Patterns.PHONE.matcher(phoneNumberField.getText().toString()).matches())
            return false;

        //Password must be at least 10 characters with upper/lower case, number(s), and special characters
        if(passwordField.getText().toString() == null ||
                !Pattern.compile(expression).matcher(passwordField.getText().toString()).matches())
            return false;

        //TODO Check if email is already registered.

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            System.out.println("Before decode file" + picturePath);
            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            System.out.println("After decode file");
        }


    }
}
