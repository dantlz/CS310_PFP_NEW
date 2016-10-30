package com.pfp.parkhere;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class VerificationActivity extends AppCompatActivity {

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
            FirebaseDatabase.getInstance().getReference().child("Peers")
                    .child(Global_ParkHere_Application
                            .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                    .child("photoID").setValue(selectedImage.toString());
        }


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
