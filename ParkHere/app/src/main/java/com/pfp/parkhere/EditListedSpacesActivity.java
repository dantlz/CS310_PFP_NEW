package com.pfp.parkhere;

import android.app.Activity;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ObjectClasses.Space;

public class EditListedSpacesActivity extends Activity {

    private Bundle extras;
    private EditText editName, editAddress, editPrice, editDescription;
    private Spinner typeSpinner, cancellationSpinner;
    private String spaceName;
    private String ownerEmail;
    private List<String> cancellationPolicies, types;
    private boolean firstTime = true;

    //TODO Allow user to change space's picture
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_spaces);

        Intent intent = getIntent();
        extras = intent.getExtras();

        findViewById(R.id.editName).setEnabled(false);

        spaceName = extras.getString("SPACE_NAME");
        ownerEmail = extras.getString("SPACE_OWNEREMAIL");
        editName = (EditText)findViewById(R.id.editName);
        editName.setText(spaceName);
        editAddress = (EditText)findViewById(R.id.editAddress);
        editPrice = (EditText)findViewById(R.id.editPrice);
        editDescription = (EditText) findViewById(R.id.editDescription);
        typeSpinner = (Spinner) findViewById(R.id.editType);
        cancellationSpinner = (Spinner) findViewById(R.id.editPolicy);
        // Spinner Drop down elements
        types = new ArrayList<String>();
        types.add("Compact");
        types.add("Truck");
        types.add("Disabled");
        cancellationPolicies = new ArrayList<String>();
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

        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Space space = dataSnapshot.getValue(Space.class);
                onCreateContinued(space);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onCreateContinued(Space space){
        if(!firstTime){
            return;
        }

        firstTime = false;

        editAddress.setText(space.getStreetAddress());
        editPrice.setText(String.valueOf(space.getPricePerHour()));
        editDescription.setText(space.getDescription());
        for(int i = 0; i < cancellationPolicies.size(); i++){
            if(cancellationPolicies.get(i).equals(space.getPolicy())){
                cancellationSpinner.setSelection(i);
            }
        }
        for(int i = 0; i < types.size(); i++){
            if(types.get(i).equals(space.getType())){
                typeSpinner.setSelection(i);
            }
        }
    }

    public void saveListedSpaceDetails(View view) {
        Address address = null;
        try {
            List<Address> addressList = new Geocoder(getApplicationContext()).getFromLocationName(editAddress.getText().toString(), 1);
            if (addressList.size() < 1){
                new AlertDialog.Builder(EditListedSpacesActivity.this, R.style.MyAlertDialogStyle)
                        .setTitle("Location not found")
                        .setMessage("We could not find a location based on your address. Please try again")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}})
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }
            else{
                address = addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("pricePerHour").setValue(Integer.valueOf(editPrice.getText().toString()));
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("type").setValue(typeSpinner.getSelectedItem().toString().toUpperCase());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("policy").setValue(cancellationSpinner.getSelectedItem().toString().toUpperCase());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("description").setValue(editDescription.getText().toString());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("city").setValue(address.getLocality());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("state").setValue(address.getAdminArea());
        Global.spaces().child(Global.getCurUser().getReformattedEmail()).child(spaceName)
                .child("streetAddress").setValue(address.getAddressLine(0));

        //TODO Allow user to change start date/time and end date/time
        startActivity(new Intent(EditListedSpacesActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void deleteListedSpace(View view) {
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).removeValue();
        startActivity(new Intent(EditListedSpacesActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

}
