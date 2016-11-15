package com.pfp.parkhere;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
import ObjectClasses.Space;
import ObjectClasses.SpaceType;

public class AddSpaceActivity extends Activity {
    private EditText spaceNameField;
    private EditText priceField;
    private EditText descriptionField;

    private EditText streetAddressField;
    private EditText cityField;
    private EditText stateField;
    private EditText countryField;
    private EditText zipCodeField;

    private DatePicker startDatePicker;
    private TimePicker startTimePicker;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;

    private Spinner typeSpinner;
    private Spinner cancellationSpinner;
    private ImageView picture;

    //TODO More Information can be set as a space's policy. Don't allow that

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);

        spaceNameField = (EditText) findViewById(R.id.spaceNameField);
        priceField = (EditText) findViewById(R.id.priceField);
        descriptionField = (EditText) findViewById(R.id.descriptionField);

        streetAddressField = (EditText) findViewById(R.id.streetAddress);
        cityField = (EditText) findViewById(R.id.city);
        stateField = (EditText) findViewById(R.id.state);
        countryField = (EditText) findViewById(R.id.country);
        zipCodeField = (EditText) findViewById(R.id.zipCode);

        startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
        startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);
        endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        cancellationSpinner = (Spinner) findViewById(R.id.spinnerForCancellation);
        cancellationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO Complete more information pop out and such
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        picture = (ImageView) findViewById(R.id.imageview);
        picture.setImageDrawable(null);

        // Spinner Drop down elements
        List<String> types = new ArrayList<String>();
        types.add("Compact");
        types.add("Truck");
        types.add("Disabled");

        List<String> cancellationPolicies = new ArrayList<String>();
        cancellationPolicies.add("Light");
        cancellationPolicies.add("Moderate");
        cancellationPolicies.add("Strict");
//        cancellationPolicies.add("More Information"); Add this back in once the listener is completed


        // Creating adapter for typeSpinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> dataCancellation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cancellationPolicies);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataCancellation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to typeSpinner
        typeSpinner.setAdapter(dataAdapter);
        cancellationSpinner.setAdapter(dataCancellation);

        if(getIntent().getExtras() != null){
            try {
                Address address = new Geocoder(this)
                        .getFromLocation(getIntent().getExtras().getDouble("LAT"),getIntent().getExtras().getDouble("LNG"), 1)
                        .get(0);
                streetAddressField.setText(address.getAddressLine(0));
                cityField.setText(address.getLocality());
                stateField.setText(address.getAdminArea());
                countryField.setText(address.getCountryName());
                zipCodeField.setText(address.getPostalCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onNewSpaceClicked(View view) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;

        //Address and latlng are from above
        //Startdate starttime enddate endtime have default values
        //email is given
        if(spaceNameField.getText().toString().equals("")||
                priceField.getText().toString().equals("")||
                descriptionField.getText().toString().equals("")||
                streetAddressField.getText().toString().equals("") ||
                cityField.getText().toString().equals("") ||
                stateField.getText().toString().equals("") ||
                countryField.getText().toString().equals("") ||
                zipCodeField.getText().toString().equals("") ||
                typeSpinner.getSelectedItem().equals(null) ||
                cancellationSpinner.getSelectedItem().equals(null)) {
            new AlertDialog.Builder(AddSpaceActivity.this, R.style.MyAlertDialogStyle)
                    .setTitle("Please complete all fields")
                    .setMessage("All input fields must be completed.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        //Check if no picture is uploaded
        if((picture.getDrawable()) == null){
            new AlertDialog.Builder(AddSpaceActivity.this, R.style.MyAlertDialogStyle)
                    .setTitle("No picture loaded")
                    .setMessage("Your space must have a picture.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        try {
            Space listedSpace = new Space();
            listedSpace.setSpaceName(spaceNameField.getText().toString());
            listedSpace.setOwnerEmail(Global.getCurUser().getEmailAddress());
            listedSpace.setType(SpaceType.valueOf(typeSpinner.getSelectedItem().toString().toUpperCase()));
            String fullAddress = streetAddressField.getText().toString() + " " +
                    cityField.getText().toString() + " " +
                    zipCodeField.getText().toString() + " " +
                    stateField.getText().toString();
            List<Address> addressResults = geocoder.getFromLocationName(fullAddress, 1);
            //Check if entered full address can be located by Geocoder because all subsequent locating is done with Geocoders
            if(addressResults.size() < 1){
                new AlertDialog.Builder(AddSpaceActivity.this, R.style.MyAlertDialogStyle)
                        .setTitle("Location not found")
                        .setMessage("We could not find a location based on your address. Please try again")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }

            listedSpace.setStreetAddress(streetAddressField.getText().toString());
            listedSpace.setCity(cityField.getText().toString());
            listedSpace.setState(stateField.getText().toString());
            listedSpace.setZipCode(zipCodeField.getText().toString());
            listedSpace.setPricePerHour(Integer.valueOf(priceField.getText().toString()));
            listedSpace.setPolicy(CancellationPolicy.valueOf(cancellationSpinner.getSelectedItem().toString().toUpperCase()));
            listedSpace.setDescription(descriptionField.getText().toString());
            listedSpace.setAvailableStartDateAndTime(new MyCalendar(
                    startDatePicker.getYear(),
                    startDatePicker.getMonth(),
                    startDatePicker.getDayOfMonth(),
                    startTimePicker.getHour(),
                    startTimePicker.getMinute()
            ));
            listedSpace.setAvailableEndDateAndTime(new MyCalendar(
                    endDatePicker.getYear(),
                    endDatePicker.getMonth(),
                    endDatePicker.getDayOfMonth(),
                    endTimePicker.getHour(),
                    endTimePicker.getMinute()
            ));
            listedSpace.setDPNonFireBase(picture.getDrawable());
            listedSpace.setSpaceRating(0);


            //TODO If an owner has a space with the same name, this should not work!!!
            Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceNameField.getText().toString()).setValue(listedSpace);
            startActivity(new Intent(AddSpaceActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        } catch (IOException e) {
            e.printStackTrace();
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

    //To create hardcoded space
//    private void setValues(){
//        CharSequence a = "test";
//        CharSequence b = "1";
//        CharSequence c = "ASKHFDJDA";
//        CharSequence d = "3335";
//        CharSequence e = "South Figueroa Street";
//        CharSequence f = "Los Angeles";
//        CharSequence g = "California";
//        CharSequence h = "United States of America";
//        CharSequence i = "90007";
//
//        spaceNameField.setText(a);
//        priceField.setText(b);
//        descriptionField.setText(c);
//        streetNumberField.setText(d);
//        streetNameField.setText(e);
//        cityField.setText(f);
//        stateField.setText(g);
//        countryField.setText(h);
//        zipCodeField.setText(i);
//    }
}
