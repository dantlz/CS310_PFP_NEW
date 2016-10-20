package com.pfp.parkhere;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddSpaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Compact");
        categories.add("Truck");
        categories.add("Disabled");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    public void onNewSpaceClicked(View view) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;

        EditText streetNumber = (EditText) findViewById(R.id.streetNumber);
        String streetNumb = streetNumber.getText().toString();
        EditText streetName = (EditText) findViewById(R.id.streetName);
        String streetNam = streetName.getText().toString();
        EditText city = (EditText) findViewById(R.id.city);
        String cityName = city.getText().toString();
        EditText country = (EditText) findViewById(R.id.country);
        String countryName = country.getText().toString();
        EditText zip = (EditText) findViewById(R.id.zipCode);
        String zipCode = zip.getText().toString();
        EditText state = (EditText) findViewById(R.id.state);
        String stateName = state.getText().toString();

        String addressName = streetNumb + " " + streetNam + ", " + cityName +", " + stateName + " " + zipCode;

        try {
            addressList = geocoder.getFromLocationName(addressName, 1);
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
