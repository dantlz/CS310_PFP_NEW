package com.pfp.parkhere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PayWithPaypalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_paypal);

    }

    public void verifyPaypal(View view) {
        EditText paypalUsernameField = (EditText)findViewById(R.id.paypal_username_field);
        EditText paypalPasswordField = (EditText)findViewById(R.id.paypal_password_field);

        String paypalUsername = paypalUsernameField.getText().toString();
        String paypalPassword = paypalPasswordField.getText().toString();

        if (paypalPassword.equals("") || paypalUsername.equals("")) {
            AlertDialog dialog;
            AlertDialog.Builder errorDisplay = new AlertDialog.Builder(view.getContext());
            errorDisplay.setTitle("ERROR!")
                    .setMessage("Ypu must enter a username and a password.");
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
