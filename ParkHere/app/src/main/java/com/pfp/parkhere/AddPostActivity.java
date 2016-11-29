package com.pfp.parkhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
import ObjectClasses.Post;

//Done sprint 2
public class AddPostActivity extends AppCompatActivity {

    public String name, ownerEmail, spaceName;
    private EditText nameField;
    private DatePicker startDatePicker, endDatePicker;
    private TimePicker startTimePicker, endTimePicker;
    private Spinner policySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        nameField = (EditText) findViewById(R.id.nameEditTextAP);
        startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
        startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);
        endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
        policySpinner = (Spinner) findViewById(R.id.policySpinner);

        ownerEmail = getIntent().getStringExtra("SPACE_OWNEREMAIL");
        spaceName = getIntent().getStringExtra("SPACE_NAME");

    }

    public void addPostButtonClicked(View view) {

        Post post = new Post();
        post.setParentOwnerEmail(ownerEmail);
        post.setParentSpaceName(spaceName);

        post.setPostName(nameField.toString());
        int n = policySpinner.getSelectedItemPosition();
        CancellationPolicy policy;
        if(n == 0)
            policy = CancellationPolicy.LIGHT;
        if(n==1)
            policy = CancellationPolicy.MODERATE;
        else
            policy = CancellationPolicy.STRICT;
        post.setPolicy(policy);

        MyCalendar startDateTime = new MyCalendar();
        startDateTime.setYear(startDatePicker.getYear());
        startDateTime.setYear(startDatePicker.getMonth());
        startDateTime.setYear(startDatePicker.getDayOfMonth());
        startDateTime.setHour(startTimePicker.getHour());
        startDateTime.setMinute(startTimePicker.getMinute());
        post.setAvailableStartDateAndTime(startDateTime);

        MyCalendar endDateTime = new MyCalendar();
        endDateTime.setYear(endDatePicker.getYear());
        endDateTime.setYear(endDatePicker.getMonth());
        endDateTime.setYear(endDatePicker.getDayOfMonth());
        endDateTime.setHour(endTimePicker.getHour());
        endDateTime.setMinute(endTimePicker.getMinute());
        post.setAvailableEndDateAndTime(endDateTime);

        Global.posts().child(Global.reformatEmail(ownerEmail)).child(spaceName).child(nameField.toString()).setValue(post);
        //TODO If the space already has a post with this name, this should not work!!!
        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("PostNames").child(nameField.toString()).setValue(nameField.toString());
    }

}
