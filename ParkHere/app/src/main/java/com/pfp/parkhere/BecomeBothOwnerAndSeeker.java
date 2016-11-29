package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ObjectClasses.Status;
//Done Sprint 2
public class BecomeBothOwnerAndSeeker extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_both_owner_and_seeker);
    }

    public void onChangeStatusClicked(View view){
        Global.curUserRef().child("status").setValue(Status.BOTH);
        startActivity(new Intent(BecomeBothOwnerAndSeeker.this, MapsMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
