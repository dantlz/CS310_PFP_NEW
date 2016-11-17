package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.util.Calendar;

import ObjectClasses.SpaceType;
//No further activity stacks here
public class SearchFiltersActivity extends Activity {

    private SpaceType type;
    private EditText lowestPriceField, highestPriceField;
    private DatePicker startDatePicker, endDatePicker;
    private TimePicker startTimePicker, endTimePicker;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        type = SpaceType.COMPACT;

        RadioButton rad = (RadioButton) findViewById(R.id.compactRadioButton);
        rad.setChecked(true);


        //Give filters very broad default values;
        lowestPriceField = (EditText) findViewById(R.id.lowestPriceField);
        highestPriceField = (EditText) findViewById(R.id.highestPriceField);
        startDatePicker = (DatePicker) findViewById(R.id.startDate);
        endDatePicker = (DatePicker) findViewById(R.id.endDate);
        startTimePicker = (TimePicker) findViewById(R.id.startTime);
        endTimePicker = (TimePicker) findViewById(R.id.endTime);

        Bundle extras = getIntent().getExtras();

        lowestPriceField.setText(String.valueOf(extras.getInt("LOWESTPRICE")));
        highestPriceField.setText(String.valueOf(extras.getInt("HIGHESTPRICE")));
        startDatePicker.updateDate(extras.getInt("STARTYEAR"), extras.getInt("STARTMONTH"), extras.getInt("STARTDAY"));
        startTimePicker.setHour(extras.getInt("STARTHOUR"));
        startTimePicker.setMinute(extras.getInt("STARTMINUTE"));
        endDatePicker.updateDate(extras.getInt("ENDYEAR"), extras.getInt("ENDMONTH"), extras.getInt("ENDDAY"));
        endTimePicker.setHour(extras.getInt("ENDHOUR"));
        endTimePicker.setMinute(extras.getInt("ENDMINUTE"));

        confirmButton = (Button) findViewById(R.id.confirmFilterButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TYPE", type);
                intent.putExtra("LOWESTPRICE", Integer.valueOf(lowestPriceField.getText().toString()));
                intent.putExtra("HIGHESTPRICE", Integer.valueOf(highestPriceField.getText().toString()));
                intent.putExtra("STARTYEAR", startDatePicker.getYear());
                intent.putExtra("STARTMONTH", startDatePicker.getMonth());
                intent.putExtra("STARTDAY", startDatePicker.getDayOfMonth());
                intent.putExtra("STARTHOUR", startTimePicker.getHour());
                intent.putExtra("STARTMINUTE", startTimePicker.getMinute());
                intent.putExtra("ENDYEAR", endDatePicker.getYear());
                intent.putExtra("ENDMONTH", endDatePicker.getMonth());
                intent.putExtra("ENDDAY", endDatePicker.getDayOfMonth());
                intent.putExtra("ENDHOUR", endTimePicker.getHour());
                intent.putExtra("ENDMINUTE", endTimePicker.getMinute());
                setResult(12321, intent);
                finish();
            }
        });
    }

    public void onTypeRadioButtonClicked(View view){
        switch (view.getId()) {
            case R.id.compactRadioButton:
                type = SpaceType.COMPACT;
                break;
            case R.id.truckRadioButton:
                type = SpaceType.TRUCK;
                break;
            case R.id.handicapRadioButton:
                type = SpaceType.DISABLED;
                break;
        }
    }
}
