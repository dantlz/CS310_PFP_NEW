package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditListedSpacesActivity extends AppCompatActivity {

    private Bundle extras;
    private EditText editName, editAddress, editPrice, editDescription;
    private Spinner typeSpinner, cancellationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_spaces);

        Intent intent = getIntent();
        extras = intent.getExtras();

        editName = (EditText)findViewById(R.id.editName);
        editName.setText(extras.getString("LISTED_SPACE_NAME"));

        editAddress = (EditText)findViewById(R.id.editAddress);
        editAddress.setText(extras.getString("LISTED_SPACE_ADDRESS"));

        editPrice = (EditText)findViewById(R.id.editPrice);
        editPrice.setText("" + extras.getDouble("LISTED_SPACE_PRICE") + "0");

        editDescription = (EditText) findViewById(R.id.editDescription);
        editDescription.setText(extras.getString("LISTED_SPACE_DESCRIPTION"));

        typeSpinner = (Spinner) findViewById(R.id.editType);
        cancellationSpinner = (Spinner) findViewById(R.id.editPolicy);
        // Spinner Drop down elements
        List<String> types = new ArrayList<String>();
        types.add("Compact");
        types.add("Truck");
        types.add("Disabled");
        List<String> cancellationPolicies = new ArrayList<String>();
        cancellationPolicies.add("Light");
        cancellationPolicies.add("Moderate");
        cancellationPolicies.add("Strict");
        // Creating adapter for typeSpinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> dataCancellation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cancellationPolicies);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataCancellation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to typeSpinner
        typeSpinner.setAdapter(dataAdapter);
        cancellationSpinner.setAdapter(dataCancellation);
        for(int i = 0; i < cancellationPolicies.size(); i++){
            if(cancellationPolicies.get(i).equals(extras.getString("LISTED_SPACE_POLICY"))){
                cancellationSpinner.setSelection(i);
            }
        }
        for(int i = 0; i < types.size(); i++){
            if(types.get(i).equals(extras.getString("LISTED_SPACE_TYPE"))){
                typeSpinner.setSelection(i);
            }
        }
    }

    public void saveListedSpaceDetails(View view) {
        String spaceName = extras.getString("LISTED_SPACE_NAME");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("spaceName").setValue(editName.getText().toString());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("pricePerHour").setValue(editPrice.getText().toString());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("type").setValue(typeSpinner.getSelectedItem().toString());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("policy").setValue(cancellationSpinner.getSelectedItem().toString());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("description").setValue(editDescription.getText().toString());
        Address address = null;
        try {
            address = new Geocoder(getApplicationContext()).getFromLocationName(editAddress.getText().toString(), 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("city").setValue(address.getLocality());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("state").setValue(address.getAdminArea());
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("country").setValue(address.getCountryName());
        String fullStreet = address.getAddressLine(0);
        String streetNumber = "";
        String streetName = "";
        for(int i = 0; i < fullStreet.length(); i++){
            if(fullStreet.charAt(i) == ' '){
                streetNumber = String.valueOf(fullStreet.substring(0, i));
                fullStreet = fullStreet.substring(i + 1);
                break;
            }
        }
        for(int i = 0; i < fullStreet.length(); i++){
            if(fullStreet.charAt(i) == ' ' || i == fullStreet.length() - 1){
                streetName = String.valueOf(fullStreet.substring(0, i));
                break;
            }
        }

        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("streetName").setValue(streetName);
        finish();
        ref.child("Spaces").child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName)
                .child("streetNumber").setValue(streetNumber);

        //TODO Edit start date and time end date and time
    }

    public void deleteListedSpace(View view) {
        String spaceName = extras.getString("LISTED_SPACE_NAME");
        FirebaseDatabase.getInstance().getReference().child("Spaces")
                .child(Global_ParkHere_Application
                .reformatEmail(Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .child(spaceName).removeValue();

        finish();
        startActivity(new Intent(EditListedSpacesActivity.this, MapsActivity.class));
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
}
