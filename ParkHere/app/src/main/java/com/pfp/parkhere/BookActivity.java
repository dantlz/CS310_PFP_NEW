package com.pfp.parkhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.Post;

//Done Sprint 2
public class BookActivity extends Activity {
//TODO Figure out simultaneous booking criss crossing
    private Post selectedPost;
    private String spaceName;
    private String ownerEmail;
    private String postName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_space);

        Bundle extras = getIntent().getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        postName = extras.getString("POST_NAME");

        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selectedPost = dataSnapshot.getValue(Post.class);
                populateFields();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void populateFields(){
        TextView nameView = (TextView)findViewById(R.id.space_name_confirmation);
        nameView.setText(selectedPost.getParentSpaceName());

        TextView postNameView = (TextView)findViewById(R.id.post_name_confirmation);
        postNameView.setText(selectedPost.getPostName());

        TextView policyView = (TextView)findViewById(R.id.display_cancellation_policy);
        policyView.setText(Global.getCancellationPolicy(selectedPost.getPolicy()));
    }

    public void activatePaymentButtons(View view) {
        CheckBox checkBox = (CheckBox)findViewById(R.id.cancellation_agreement_verifier);

        Button cardButton = (Button)findViewById(R.id.pay_with_card_button);
        Button paypalButton = (Button)findViewById(R.id.pay_with_paypal_button);

        if (checkBox.isChecked()) {
            cardButton.setEnabled(true);
            paypalButton.setEnabled(true);
        }
        else {
            cardButton.setEnabled(false);
            paypalButton.setEnabled(false);
        }
    }

    public void payWithCard(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, BookCardPaymentActivity.class);

        Bundle extras = new Bundle();
        extras.putString("SPACE_OWNEREMAIL", selectedPost.getParentOwnerEmail());
        extras.putString("SPACE_NAME", selectedPost.getParentSpaceName());
        extras.putString("POST_NAME", selectedPost.getPostName());

        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    public void payWithPaypal(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, BookCardPaymentActivity.class);

        Bundle extras = new Bundle();
        extras.putString("SPACE_OWNEREMAIL", selectedPost.getParentOwnerEmail());
        extras.putString("SPACE_NAME", selectedPost.getParentSpaceName());
        extras.putString("POST_NAME", selectedPost.getPostName());

        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
}
