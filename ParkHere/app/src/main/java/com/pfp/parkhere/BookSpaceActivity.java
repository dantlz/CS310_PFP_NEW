package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;

import ObjectClasses.Space;

public class BookSpaceActivity extends AppCompatActivity {

    Space selectedSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_space);

        selectedSpace = new Space();

        selectedSpace.setSpaceName("USC New/North Parking");
        selectedSpace.setStreetAddress("635 McCarthy Way");
        selectedSpace.setCity("Los Angeles");
        selectedSpace.setState("CA");
        selectedSpace.setZipCode("90007");
        selectedSpace.setPricePerHour(10);
        selectedSpace.setOwnerEmail("bradfora@usc.edu");

        TextView nameView = (TextView)findViewById(R.id.space_name_confirmation);
        nameView.setText(selectedSpace.getSpaceName() + "\n");

        TextView addressView = (TextView)findViewById(R.id.space_address_confirmation);
        addressView.setText(selectedSpace.getStreetAddress() + ",\n"
            + selectedSpace.getCity() + ", " + selectedSpace.getState() + ", " + selectedSpace.getZipCode() + "\n");

        TextView priceView = (TextView)findViewById(R.id.space_price_confirmation);
        priceView.setText("Price: $" + selectedSpace.getPricePerHour());


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
        extras.putString("OWNER_EMAIL_IDENTIFIER", selectedSpace.getOwnerEmail());
        extras.putString("OWNER_NAME_IDENTIFIER", selectedSpace.getSpaceName());

        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public void payWithPaypal(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, PayWithPaypalActivity.class);


        Bundle extras = new Bundle();
        extras.putString("OWNER_EMAIL_IDENTIFIER", selectedSpace.getOwnerEmail());
        extras.putString("OWNER_NAME_IDENTIFIER", selectedSpace.getSpaceName());

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
