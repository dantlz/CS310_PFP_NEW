package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
import ObjectClasses.Post;
import ObjectClasses.Space;

public class EditPostActivity extends AppCompatActivity {

    Post currPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        currPost = populatePost();

        TextView postName = (TextView)findViewById(R.id.view_name_edit_post);
        postName.setText(currPost.getPostName());

        TextView postSpaceName = (TextView)findViewById(R.id.view_spaceName_edit_post);
        postSpaceName.setText("From Space: " + currPost.getParentSpaceName());

        Spinner postCancellationPolicies = (Spinner)findViewById(R.id.cancellation_spinner);
        ArrayList<String> cancellationPolicies = new ArrayList<>();
        cancellationPolicies.add("Light");
        cancellationPolicies.add("Moderate");
        cancellationPolicies.add("Strict");
        ArrayAdapter<String> policyAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,
                cancellationPolicies);
        postCancellationPolicies.setAdapter(policyAdapter);


    }

    private Post populatePost() {
        Post retPost = new Post();

        retPost.setPostName("USC Colosseum");
        retPost.setParentOwnerEmail("bradfora@usc.edu");
        retPost.setParentSpaceName("USC Parking");
        retPost.setPolicy(CancellationPolicy.MODERATE);

        MyCalendar startDate = new MyCalendar(2016, 11, 20, 10, 30);
        MyCalendar endDate = new MyCalendar(2016, 11, 30, 5, 00);

        retPost.setAvailableStartDateAndTime(startDate);
        retPost.setAvailableEndDateAndTime(endDate);

        return retPost;

    }

    public void goToEditSpace(View view) {
        Intent intent = new Intent(EditPostActivity.this, EditListedSpacesActivity.class);
        intent.putExtra("SPACE_OWNEREMAIL", currPost.getParentOwnerEmail());
        intent.putExtra("SPACE_NAME", currPost.getParentSpaceName());
        startActivity(intent);
        finish();
    }

    public void savePostChanges(View view) {
        Spinner cancellationSpinner = (Spinner)findViewById(R.id.cancellation_spinner);
        currPost.setPolicy(CancellationPolicy.valueOf(cancellationSpinner.getSelectedItem().toString().toUpperCase()));

        DatePicker startDate = (DatePicker)findViewById(R.id.edit_post_start_date);
        TimePicker startTime = (TimePicker)findViewById(R.id.edit_post_start_time);
        currPost.setAvailableStartDateAndTime(new MyCalendar(
                startDate.getYear(),
                startDate.getMonth(),
                startDate.getDayOfMonth(),
                startTime.getHour(),
                startTime.getMinute()
        ));

        DatePicker endDate = (DatePicker)findViewById(R.id.edit_post_end_date);
        TimePicker endTime = (TimePicker)findViewById(R.id.edit_post_end_time);
        currPost.setAvailableEndDateAndTime(new MyCalendar(
                endDate.getYear(),
                endDate.getMonth(),
                endDate.getDayOfMonth(),
                endTime.getHour(),
                endTime.getMinute()
        ));

        startActivity(new Intent(EditPostActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
