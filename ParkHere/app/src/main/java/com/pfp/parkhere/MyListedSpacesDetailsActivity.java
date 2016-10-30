package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyListedSpacesDetailsActivity extends AppCompatActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces_details);

        Intent intent = getIntent();
        extras = intent.getExtras();

        TextView nameText = (TextView)findViewById(R.id.listed_space_name);
        nameText.setText(extras.getString("LISTED_SPACE_NAME"));

        TextView addressText = (TextView)findViewById(R.id.listed_space_address);
        addressText.setText(extras.getString("LISTED_SPACE_ADDRESS"));
        addressText.append(",\n" + extras.getString("LISTED_SPACE_CITY"));
        addressText.append(", " + extras.getString("LISTED_SPACE_STATE"));
        addressText.append(", " + extras.getString("LISTED_SPACE_ZIP"));

        TextView priceText = (TextView)findViewById(R.id.listed_space_price);
        priceText.setText("Price: $" + extras.getDouble("LISTED_SPACE_PRICE") + "0");

    }

    public void onEditListedSpaceClicked(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, EditListedSpacesActivity.class);

        intent.putExtras(extras);
        context.startActivity(intent);
    }
}
