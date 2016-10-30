package com.pfp.parkhere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        loginButton = (Button) findViewById(R.id.loginButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fireBaseUser = firebaseAuth.getCurrentUser();
                if(fireBaseUser != null) {
                    //User is signed in
                    //TODO Get the login type SEEKER/OWNER


                    System.out.println(fireBaseUser.getEmail());
                    FirebaseDatabase.getInstance()
                            .getReference("Seekers")
                            .child(Global_ParkHere_Application.reformatEmail(fireBaseUser.getEmail()))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    //TODO Seekers AND owners
                                    Peer currentUser = dataSnapshot.getValue(Peer.class);
                                    ((Global_ParkHere_Application) getApplication()).setCurrentUserObject(currentUser);
                                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });
                }
                else{
                    // User is signed out. This should never happen
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                firebaseSignIn(emailField.getText().toString(), passwordField.getText().toString());
            }
        });
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
                        if (!task.isSuccessful()){
                            if(task.getException().getClass().equals(FirebaseAuthInvalidCredentialsException.class)){
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Email or password invalid")
                                        .setMessage("The entered email or password is invalid. " +
                                                "Please retry or register")
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

                        //Other wise, the user is successfully signed in. Further action in mAuthListener
                    }
                });
    }

    //TODO login registrer add buttons to go to each other
    //TODO option to login/register as owner AND seeker
    //TODO Add these logouts to MapsActivity as well!!!
    @Override
    protected void onDestroy() {
        mAuth.signOut();
        super.onDestroy();
    }

    //TODO UNCOMMENT THIS
//    @Override
//    protected void onStop() {
//        mAuth.signOut();
//        super.onStop();
//    }
}
