package com.pfp.parkhere;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Peer;

public class LoginActivity extends Activity {

    //Firebase objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button goToRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loginLoad).setVisibility(View.GONE);

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

                    if(!fireBaseUser.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        return;
                    
                    Global.peers().child(Global.reformatEmail(fireBaseUser.getEmail()))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    Peer currentUser = dataSnapshot.getValue(Peer.class);
                                    if(currentUser == null){
                                        return;
                                    }
                                    Global.setCurUser(currentUser);
                                    startActivity(new Intent(LoginActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                    return;
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
                String message = "";
                if(emailField.getText().toString().equals(""))
                    message = "Please enter an email address";
                else if(passwordField.getText().toString().equals(""))
                    message = "Please enter your password";
                if(!message.equals("")){
                    new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle)
                            .setTitle("Missing email or password")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                firebaseSignIn(emailField.getText().toString(), passwordField.getText().toString());
            }
        });
        goToRegisterButton = (Button) findViewById(R.id.goToRegisterButton);
        goToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    //Firebase sign in
    private void firebaseSignIn(String email, String password){
        findViewById(R.id.loginLoad).setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        findViewById(R.id.loginLoad).setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()){
                                new AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle)
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

                        //Other wise, the user is successfully signed in. Further action in mAuthListener
                    }
                });
    }


}
