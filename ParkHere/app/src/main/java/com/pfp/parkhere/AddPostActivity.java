package com.pfp.parkhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class AddPostActivity extends AppCompatActivity {

    public String name;
    private EditText nameField;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private Spinner policySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        nameField = (EditText) findViewById(R.id.nameEditTextAP);
        fromDatePicker = (DatePicker) findViewById(R.id.fromDatePicker);
        toDatePicker = (DatePicker) findViewById(R.id.toDatePicker);
        policySpinner = (Spinner) findViewById(R.id.policySpinner);

    }

    void addPostButtonClicked(View view)
    {
        //Send all the info entered to server to create post
        //Take the info from the private member variables nameField, fromDatePicker, toDatePicker and policySpinner.
        //Something like what's below.

        String name = nameField.toString();
        int d1 = fromDatePicker.getDayOfMonth();
        int m1 = fromDatePicker.getMonth();
        int y1 = fromDatePicker.getYear();
        int d2 = toDatePicker.getDayOfMonth();
        int m2 = toDatePicker.getMonth();
        int y2 = toDatePicker.getYear();
        String policy = "";

        int n = policySpinner.getSelectedItemPosition();
        if(n == 0)
        {
            policy = "Light";
        }
        if(n==1)
        {
            policy = "Moderate";
        }
        if(n==2)
        {
            policy = "Strict";
        }
        else
        {
            //No policy selected. Won't ever happen because it always selects 0 by default.
        }

        //Now just send this info (as contained in the variables) to the server and create a post.

    }

}
