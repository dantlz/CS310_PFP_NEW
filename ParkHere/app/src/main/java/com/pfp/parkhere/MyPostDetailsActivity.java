package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Post;
import ObjectClasses.Status;

//Done Sprint 2
//DOUBLEUSE
public class MyPostDetailsActivity extends Activity {
    private Button editButton, bookPostButton, finishBookingButton;

    private String spaceName;
    private String ownerEmail;
    private String postName;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        firstTime = true;

        editButton = (Button) findViewById(R.id.postdetail_edit_listed_space_button);
        bookPostButton = (Button) findViewById(R.id.postdetail_bookSpaceButton);
        finishBookingButton = (Button) findViewById(R.id.postdetail_finishBookingsButton);

        Bundle extras = getIntent().getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        postName =  extras.getString("POST_NAME");

        //Can't book my own space
        if(ownerEmail.equals(Global.getCurUser().getEmailAddress())){
            bookPostButton.setVisibility(View.GONE);
        }
        //Can book or look at someone else, but can't modify their stuff
        else{
            finishBookingButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        //Owners can't book space, but can view other owners, confirm/edit his own
        if(Global.getCurUser().getPreferredStatus().equals(Status.OWNER)){
            bookPostButton.setVisibility(View.GONE);
        }
        //Seekers can book space or look at owner, but can't confirm or edit.
        else{
            finishBookingButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }


        finishBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetailsActivity.this, FinishBookingsActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                intent.putExtra("POST_NAME", postName);
                startActivity(intent);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetailsActivity.this, EditPostActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                intent.putExtra("POST_NAME", postName);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        bookPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostDetailsActivity.this, BookActivity.class);
                intent.putExtra("SPACE_NAME", spaceName);
                intent.putExtra("SPACE_OWNEREMAIL", ownerEmail);
                intent.putExtra("POST_NAME", postName);
                startActivity(intent);
                finish();
            }
        });


        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post= dataSnapshot.getValue(Post.class);
                onCreateContinued(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCreateContinued(Post post){
        if(!firstTime){
            return;
        }
        firstTime = false;

        //Came from my listed space detail
        TextView nameText = (TextView) findViewById(R.id.postdetail_post_name);
        nameText.setText(post.getPostName());

        TextView policyField = (TextView) findViewById(R.id.postdetail_policyField);
        policyField.setText(String.valueOf(post.getPolicy()));
    }

}
