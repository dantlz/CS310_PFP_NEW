package com.pfp.parkhere;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
//Done Sprint 2
//TODO Name change: xml files need to have their names changed to match java files
public class EditPostActivity extends Activity {

    private String ownerEmail, spaceName, postName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postName = getIntent().getExtras().getString("POST_NAME");
        ownerEmail = getIntent().getExtras().getString("SPACE_OWNEREMAIL");
        spaceName = getIntent().getExtras().getString("SPACE_NAME");

        TextView postNameField = (TextView)findViewById(R.id.view_name_edit_post);
        postNameField.setText(postName);

        TextView postSpaceName = (TextView)findViewById(R.id.view_spaceName_edit_post);
        postSpaceName.setText("From Space: " + spaceName);


        Spinner postCancellationPolicies = (Spinner)findViewById(R.id.cancellation_spinner);
        ArrayList<String> cancellationPolicies = new ArrayList<>();
        cancellationPolicies.add("Light");
        cancellationPolicies.add("Moderate");
        cancellationPolicies.add("Strict");
        ArrayAdapter<String> policyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                cancellationPolicies);
        postCancellationPolicies.setAdapter(policyAdapter);

    }

    public void deletePost(View view) {
        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).removeValue();
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("PostNames").child(postName).removeValue();
        //TODO Deleting a post must delete bookings too, or it can't be deleted until bookings are done.
        finish();
    }

    public void savePostChanges(View view) {
        Spinner cancellationSpinner = (Spinner)findViewById(R.id.cancellation_spinner);
        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).child("policy").
                setValue(CancellationPolicy.valueOf(cancellationSpinner.getSelectedItem().toString().toUpperCase()));

        DatePicker startDate = (DatePicker)findViewById(R.id.edit_post_start_date);
        TimePicker startTime = (TimePicker)findViewById(R.id.edit_post_start_time);
        MyCalendar startDateTime = new MyCalendar(
                startDate.getYear(),
                startDate.getMonth(),
                startDate.getDayOfMonth(),
                startTime.getHour(),
                startTime.getMinute()
        );
        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).child("availableStartDateAndTime").
                setValue(startDateTime);


        DatePicker endDate = (DatePicker)findViewById(R.id.edit_post_end_date);
        TimePicker endTime = (TimePicker)findViewById(R.id.edit_post_end_time);
        MyCalendar endDateTime = new MyCalendar(
                endDate.getYear(),
                endDate.getMonth(),
                endDate.getDayOfMonth(),
                endTime.getHour(),
                endTime.getMinute()
        );
        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(postName).child("availableEndDateAndTime").
                setValue(endDateTime);

        finish();
    }
}
