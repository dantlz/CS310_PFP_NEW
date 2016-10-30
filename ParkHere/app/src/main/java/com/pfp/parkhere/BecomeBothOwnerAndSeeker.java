package com.pfp.parkhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ObjectClasses.Status;

public class BecomeBothOwnerAndSeeker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_both_owner_and_seeker);
    }

    public void changeStatus(View view){
        FirebaseDatabase.getInstance().getReference().child("Peers").child(
                Global_ParkHere_Application.reformatEmail(
                Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child("status").setValue(Status.BOTH);
        finish();
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
