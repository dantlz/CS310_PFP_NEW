package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.pfp.parkhere.MapsActivity;

public class AddSpaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onNewSpaceClicked(View view) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
//
//        EditText streetNumber = (EditText) findViewById(R.id.streetNumber);
//        String streetNumb = streetNumber.getText().toString();
//        EditText streetName = (EditText) findViewById(R.id.streetName);
//        String streetNam = streetName.getText().toString();
//        EditText city = (EditText) findViewById(R.id.city);
//        String cityName = city.getText().toString();
//        EditText country = (EditText) findViewById(R.id.country);
//        String countryName = country.getText().toString();
//        EditText zip = (EditText) findViewById(R.id.zipCode);
//        String zipCode = zip.getText().toString();
//        EditText state = (EditText) findViewById(R.id.state);
//        String stateName = state.getText().toString();

//        String addressName = streetNumb + " " + streetNam + ", " + cityName + ", " + stateName + " " + zipCode;
        MapsActivity mMapActivity = new MapsActivity();
        String addressName = "1202 W 29th Street, Los Angeles, CA 90007";
        startActivity(new Intent(AddSpaceActivity.this, MapsActivity.class));
        mMapActivity.addSpace(addressName);


//        try {
//            addressList = geocoder.getFromLocationName(addressName, 1);
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            MapsActivity mMapActivity= new MapsActivity();
//            mMapActivity.addNewSpot(latLng);
//            startActivity(new Intent(AddSpaceActivity.this, MapsActivity.class));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddSpace Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
