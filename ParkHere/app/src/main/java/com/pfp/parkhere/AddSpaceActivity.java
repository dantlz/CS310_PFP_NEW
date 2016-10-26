package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

import static com.pfp.parkhere.R.layout.cancellation_policy_popup;


public class AddSpaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static int RESULT_LOAD_IMAGE = 1;
    private String streetNumb = null;
    private String streetNam = null;
    private String cityName = null;
    private String zipCode = null;
    private String stateName = null;
    private LatLng latLng = null;
    private String addressName = null;
    private PopupWindow mPopupWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create Spinners
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinnerForCancellation = (Spinner) findViewById(R.id.spinnerForCancellation);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinnerForCancellation.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Compact");
        categories.add("Truck");
        categories.add("Disabled");

        List<String> cancellationCategories = new ArrayList<String>();
        cancellationCategories.add("Light");
        cancellationCategories.add("Medium");
        cancellationCategories.add("Sever");
        cancellationCategories.add("More Information");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataCancellation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cancellationCategories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataCancellation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinnerForCancellation.setAdapter(dataCancellation);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onNewSpaceClicked(View view) {

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;

        EditText streetNumber = (EditText) findViewById(R.id.streetNumber);
        if (streetNumber.toString().length() != 0) {
            streetNumb = streetNumber.getText().toString();
        }
        EditText streetName = (EditText) findViewById(R.id.streetName);
        if (streetName.toString().length() != 0) {
            streetNam = streetName.getText().toString();
        }
        EditText city = (EditText) findViewById(R.id.city);
        if (city.toString().length() != 0) {
            cityName = city.getText().toString();
        }
        EditText zip = (EditText) findViewById(R.id.zipCode);
        if (zip.toString().length() != 0){
            zipCode = zip.getText().toString();
        }

        EditText state = (EditText) findViewById(R.id.state);
        if (state.toString().length() != 0) {
            stateName = state.getText().toString();
        }

        if (streetNumb == null | streetNam==null | cityName==null | stateName==null |zipCode==null) {
            System.out.println("here");

        }
        else {
            addressName = streetNumb + " " + streetNam + ", " + cityName + ", " + stateName + " " + zipCode;

            try {
                addressList = geocoder.getFromLocationName(addressName, 1);
                Address address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //TODO: Add these coordinates to the Firebase list of avaliable spaces
                startActivity(new Intent(AddSpaceActivity.this, MapsActivity.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onLoadPictureClicked(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageview = (ImageView) findViewById(R.id.imageview);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                }
                break;
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        System.out.println(item);

        if (item.equals("More Information")) {
            moreInfoClicked();
        }

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void moreInfoClicked() {
        System.out.println("Here");
//        LayoutInflater layoutInflater
//                = (LayoutInflater)getBaseContext()
//                .getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = layoutInflater.inflate(popup, null);
//        final PopupWindow popupWindow = new PopupWindow(
//                popupView,
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.isShowing();
        Context mContext = getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(cancellation_policy_popup,null);

        mPopupWindow = new PopupWindow(
                popup,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_add_space);
        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

        Button closeButton = (Button) popup.findViewById(R.id.dismissButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                System.out.println("on Click");
                mPopupWindow.dismiss();
            }
        });

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
