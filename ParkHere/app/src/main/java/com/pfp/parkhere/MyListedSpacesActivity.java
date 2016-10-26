package com.pfp.parkhere;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.SynchronousQueue;

import ObjectClasses.Space;


public class MyListedSpacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create a set of spaces that will later be passed in on creation of the activity
        SynchronousQueue<Space> mySpaces = new SynchronousQueue<Space>();

        Space SampleSpace1 = new Space();
        Space SampleSpace2 = new Space();
        Space SampleSpace3 = new Space();
        Space SampleSpace4 = new Space();

        SampleSpace1.setName("USC Parking");
        SampleSpace2.setName("Colliseum Parking");
        SampleSpace3.setName("New/North Parking");
        SampleSpace4.setName("Doheny Parking");

        mySpaces.add(SampleSpace1);
        mySpaces.add(SampleSpace2);
        mySpaces.add(SampleSpace3);
        mySpaces.add(SampleSpace4);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_my_listed_spaces, null);

        LinearLayout spaceLayout = (LinearLayout) view.findViewById(R.id.list_of_my_spaces);
        LinearLayout buttonLayout = (LinearLayout) view.findViewById(R.id.view_my_space_buttons);

        for (int i = 0; i < mySpaces.size(); i++) {
            TextView newSpace = new TextView(this);
            newSpace.setText(mySpaces.remove().getName());
            newSpace.setTextSize(16);

            Button newButton = new Button(this);
            newButton.setText("View Space");
            newButton.setTextSize(16);

            spaceLayout.addView(newSpace);
            buttonLayout.addView(newButton);
        }

        setContentView(R.layout.activity_my_listed_spaces);
    }
}
