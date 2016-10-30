package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MyListedSpacesDetailsActivity extends AppCompatActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces_details);

        Intent intent = getIntent();
        extras = intent.getExtras();

        TextView nameText = (TextView)findViewById(R.id.listed_space_name);
        nameText.setText(extras.getString("LISTED_SPACE_NAME"));

        TextView addressText = (TextView)findViewById(R.id.listed_space_address);
        addressText.setText(extras.getString("LISTED_SPACE_ADDRESS"));
        addressText.append(",\n" + extras.getString("LISTED_SPACE_CITY"));
        addressText.append(", " + extras.getString("LISTED_SPACE_STATE"));
        addressText.append(", " + extras.getString("LISTED_SPACE_ZIP"));

        TextView priceText = (TextView)findViewById(R.id.listed_space_price);
        priceText.setText("Price: $" + extras.getDouble("LISTED_SPACE_PRICE") + "0");

        TextView typeField = (TextView) findViewById(R.id.typeField);
        typeField.setText(extras.getString("LISTED_SPACE_TYPE"));

        TextView policyField = (TextView) findViewById(R.id.policyField);
        policyField.setText(extras.getString("LISTED_SPACE_POLICY"));

        TextView descriptionField = (TextView) findViewById(R.id.descriptionField);
        descriptionField.setText(extras.getString("LISTED_SPACE_DESCRIPTION"));

        TextView spaceRatingField = (TextView) findViewById(R.id.spaceRatingField);
        spaceRatingField.setText(extras.getString("LISTED_SPACE_RATING"));

        TextView spaceReviewField = (TextView) findViewById(R.id.spaceReviewField);
        spaceReviewField.setText(extras.getString("LISTED_SPACE_REVIEW"));


        Button ownerButton = (Button) findViewById(R.id.ownerButton);
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListedSpacesDetailsActivity.this, ProfileActivity.class);
                intent.putExtra("LISTED_SPACE_OWNEREMAIL", extras.getStringArrayList("LISTED_SPACE_OWNEREMAIL"));
                startActivity(new Intent());
            }
        });
    }

    public void onEditListedSpaceClicked(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, EditListedSpacesActivity.class);

        intent.putExtras(extras);
        context.startActivity(intent);
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
