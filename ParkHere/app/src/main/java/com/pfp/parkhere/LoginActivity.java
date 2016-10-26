package com.pfp.parkhere;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ObjectClasses.Owner;
import ObjectClasses.Peer;

public class LoginActivity extends AppCompatActivity {

    //Firebase objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (EditText)findViewById(R.id.login_email_field);
        passwordField = (EditText) findViewById(R.id.login_password_field);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fireBaseUser = firebaseAuth.getCurrentUser();
                if (fireBaseUser != null) {
                    // User is signed in
                    //Create intent

                    //TODO Get the login type SEEKER/OWNER
                    Peer user = new Owner();
                    //TODO Get email address from firebase
                    user.setEmailAddress(null);
                    ((Global_ParkHere_Application)getApplication()).setCurrentUserObject(null);
                } else {
                    // User is signed out
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Firebase sign in
    private void firebaseSignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //USER LOGIN FAILED
                            Toast.makeText(null, "LOGIN FAILED", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
