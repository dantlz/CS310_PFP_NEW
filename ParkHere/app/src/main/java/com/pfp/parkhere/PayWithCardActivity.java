package com.pfp.parkhere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.MenuRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ActionProvider;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

        String error = "";

        EditText cardNumberField = (EditText)findViewById(R.id.card_number_value);
        EditText cardDateField = (EditText)findViewById(R.id.card_date_value);
        EditText cardNameField = (EditText)findViewById(R.id.card_name_value);
        EditText cardCvField = (EditText)findViewById(R.id.card_cv_value);

        String cardNumber = cardNumberField.getText().toString();
        String cardDate = cardDateField.getText().toString();
        String cardName = cardNameField.getText().toString();
        String cardCV = cardCvField.getText().toString();

        if (cardNumber.length() != 16) {
            error += "Card number should be exactly 16 digits.\n";
        }
        if (cardDate.length() != 4) {
            error += "Card expiration date should be exactly 4 digits.\n";
        }
        if (cardDate.length() != 0) {
            if (cardDate.charAt(0) != '0' && cardDate.charAt(0) != '1') {
                error += "Detected card expiration date is not a valid date. Enter as mmyy.\n";
            }
        }
        if (cardName.length() == 0 || !cardName.contains(" ")) {
            error += "Detected invalid name format. Enter first and last names of the cardholder.\n";
        }
        if (cardCV.length() != 3) {
            error += "Card CV code should be at exactly 3 digits.\n";
        }

        if (!error.equals("")) {
            AlertDialog dialog;
            AlertDialog.Builder errorDisplay = new AlertDialog.Builder(view.getContext());
            errorDisplay.setTitle("ERROR!")
                    .setMessage(error);
            errorDisplay.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            dialog = errorDisplay.create();
            dialog.show();
        }

        else {
            Context context = view.getContext();
            Intent intent = new Intent(context, MyBookingsActivity.class);

            startActivity(intent);
        }
    }
}
