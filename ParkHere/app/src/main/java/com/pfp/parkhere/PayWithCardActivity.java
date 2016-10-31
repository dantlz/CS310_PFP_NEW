package com.pfp.parkhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PayWithCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_card);

        List<String> cardTypeText = new ArrayList<String>();
        cardTypeText.add("Visa");
        cardTypeText.add("MasterCard");
        cardTypeText.add("American Express");

        ArrayAdapter<String> cardTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cardTypeText);

        Spinner cardSelector = (Spinner)findViewById(R.id.cboose_card_spinner);
        cardSelector.setAdapter(cardTypeAdapter);

    }

    public void finishPayment(View view) {

    }
}
