package com.pfp.parkhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.Space;

public class BookSpaceActivity extends Activity {

    private Space selectedSpace;
    private String spaceName;
    private String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_space);

        Bundle extras = getIntent().getExtras();
        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");

        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selectedSpace = dataSnapshot.getValue(Space.class);
                populateFields();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void populateFields(){
        TextView nameView = (TextView)findViewById(R.id.space_name_confirmation);
        nameView.setText(selectedSpace.getSpaceName());

        TextView addressView = (TextView)findViewById(R.id.space_address_confirmation);
        addressView.setText(selectedSpace.getStreetAddress() + ",\n"
                + selectedSpace.getCity() + ", " + selectedSpace.getState() + ", " + selectedSpace.getZipCode());

        TextView priceView = (TextView)findViewById(R.id.space_price_confirmation);
        priceView.setText("Price: $" + selectedSpace.getPricePerHour());

        TextView policyView = (TextView)findViewById(R.id.display_cancellation_policy);
        policyView.setText(Global.getCancellationPolicy(selectedSpace.getPolicy()));

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
        Intent intent = new Intent(context, PayWithCardActivity.class);

        Bundle extras = new Bundle();
        extras.putString("SPACE_OWNEREMAIL", selectedSpace.getOwnerEmail());
        extras.putString("SPACE_NAME", selectedSpace.getSpaceName());

        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    public void payWithPaypal(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, PayWithPaypalActivity.class);

        Bundle extras = new Bundle();
        extras.putString("SPACE_OWNEREMAIL", selectedSpace.getOwnerEmail());
        extras.putString("SPACE_NAME", selectedSpace.getSpaceName());

        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
}
