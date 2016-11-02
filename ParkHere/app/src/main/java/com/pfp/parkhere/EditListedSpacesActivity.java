package com.pfp.parkhere;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditListedSpacesActivity extends AppCompatActivity {

    private Bundle extras;
    private EditText editName, editAddress, editPrice, editDescription;
    private Spinner typeSpinner, cancellationSpinner;

    //TODO Allow user to change space's picture
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
        editPrice.setText("" + extras.getInt("LISTED_SPACE_PRICE"));

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

        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("spaceName").setValue(editName.getText().toString());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("pricePerHour").setValue(Integer.valueOf(editPrice.getText().toString()));
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("type").setValue(typeSpinner.getSelectedItem().toString().toUpperCase());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("policy").setValue(cancellationSpinner.getSelectedItem().toString().toUpperCase());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("description").setValue(editDescription.getText().toString());
        Address address = null;
        try {
            List<Address> addressList = new Geocoder(getApplicationContext()).getFromLocationName(editAddress.getText().toString(), 1);
            if (addressList.size() < 1){
                new AlertDialog.Builder(EditListedSpacesActivity.this)
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("city").setValue(address.getLocality());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("state").setValue(address.getAdminArea());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("streetAddress").setValue(address.getAddressLine(0));

        //TODO Allow user to change start date/time and end date/time
        finish();
    }

    public void deleteListedSpace(View view) {
        String spaceName = extras.getString("LISTED_SPACE_NAME");
        String ownerEmail = extras.getString("OWNEREMAIL");
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).removeValue();
        startActivity(new Intent(EditListedSpacesActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

}
