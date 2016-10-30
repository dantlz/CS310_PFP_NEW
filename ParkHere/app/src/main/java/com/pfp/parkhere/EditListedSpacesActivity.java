package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class EditListedSpacesActivity extends AppCompatActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_spaces);

        Intent intent = getIntent();
        extras = intent.getExtras();

        EditText nameTextField = (EditText)findViewById(R.id.edit_name_field);
        nameTextField.setText(extras.getString("LISTED_SPACE_NAME"));

        EditText addressTextField = (EditText)findViewById(R.id.edit_address_field);
        addressTextField.setText(extras.getString("LISTED_SPACE_ADDRESS"));

        EditText priceTextField = (EditText)findViewById(R.id.edit_price_field);
        priceTextField.setText("" + extras.getDouble("LISTED_SPACE_PRICE") + "0");
    }

    public void saveListedSpaceDetails(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MyListedSpacesDetailsActivity.class);

        //TODO SAVE TO FIREBASE HERE

        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public void deleteListedSpace(View view) {
        //TODO DELETE from firebase

        Context context = view.getContext();
        Intent intent = new Intent(context, MyListedSpacesActivity.class);

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
