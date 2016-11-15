package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class VerificationActivity extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        buttonLoadImage = (Button) findViewById(R.id.uploadPictureButton);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Global.peers().child(Global.getCurUser().getReformattedEmail()).child("photoID").setValue(selectedImage.toString());
            startActivity(new Intent(VerificationActivity.this, MapsActivity.class));
            finish();
        }


    }

}
