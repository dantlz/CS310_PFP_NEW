package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditListedSpacesActivity extends AppCompatActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listed_spaces);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        EditText nameTextField = (EditText)findViewById(R.id.edit_name_field);
        nameTextField.setText(extras.getString("LISTED_SPACE_NAME"));

        EditText addressTextField = (EditText)findViewById(R.id.edit_address_field);
        addressTextField.setText(extras.getString("LISTED_SPACE_ADDRESS"));

        EditText priceTextField = (EditText)findViewById(R.id.edit_price_field);
        priceTextField.setText(extras.getString("LISTED_SPACE_PRICE"));
    }

    public void saveListedSpaceDetails(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MyListedSpacesDetailsActivity.class);

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
