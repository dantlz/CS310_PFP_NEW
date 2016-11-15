package com.pfp.parkhere;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;
import ObjectClasses.Space;

//TODO Firebase - Add field to Peer: available balance
public class FinishBookingsActivity extends Activity {

    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
    private String spaceName;
    private String ownerEmail;
    private List<String> listOfBookingIdentifiers;
    private List<String> listOfBookingSeekerEmails;
    private List<Booking> allBookings;
    private Button finishButton;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_bookings);
        firstTime = true;

        finishButton = (Button) findViewById(R.id.finish_bookings_button);

        allBookings = new LinkedList<>();
        listOfBookingIdentifiers = new LinkedList<>();
        listOfBookingSeekerEmails = new LinkedList<>();

        spaceName = getIntent().getExtras().getString("SPACE_NAME");
        ownerEmail = getIntent().getExtras().getString("SPACE_OWNEREMAIL");

        Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("currentBookingIdentifiers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot id: dataSnapshot.getChildren()){
                            listOfBookingIdentifiers.add(id.getKey());
                            listOfBookingSeekerEmails.add(id.getValue(String.class));
                        }

                        generateBookings();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void generateBookings() {
        if(!firstTime){
            return;
        }

        Global.bookings().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < listOfBookingSeekerEmails.size(); i ++){
                    String email = listOfBookingSeekerEmails.get(i);
                    String bookingIdentifier = listOfBookingIdentifiers.get(i);
                    Booking booking = dataSnapshot.child(Global.reformatEmail(email)).child(bookingIdentifier).getValue(Booking.class);
                    allBookings.add(booking);
                }
                populateFields();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void populateFields() {
        if(!firstTime){
            return;
        }
        firstTime = false;

        TextView nameOfSpace = (TextView) findViewById(R.id.space_name_for_bookings);
        if (allBookings.size() != 0) {
            nameOfSpace.setText(spaceName);
        }
        else {
            nameOfSpace.setText("This space has no active bookings.");
            finishButton.setEnabled(false);
            return;
        }


        ListView bookingsDisplay = (ListView)findViewById(R.id.list_of_bookings);
        String [] bookingItemDisplayText = new String[allBookings.size()];

        for (int i = 0; i < allBookings.size(); i++) {
            Booking booking = allBookings.get(i);

            MyCalendar startTime = booking.getStartCalendarDate();
            MyCalendar endTime = booking.getEndCalendarDate();
            bookingItemDisplayText[i] = booking.getSpaceName() + "\n"
                    + startTime.getMonth() + "/"
                    + startTime.getDay() + "/"
                    + startTime.getYear() + " "
                    + String.format("%02d", startTime.getHour()) +":"
                    + String.format("%02d", startTime.getMinute()) + " - "
                    + endTime.getMonth() + "/"
                    + endTime.getDay() + "/"
                    + endTime.getYear() + " "
                    + String.format("%02d", endTime.getHour()) + ":"
                    + String.format("%02d", endTime.getMinute());
        }

        ArrayAdapter<String> adapterForBookings = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, bookingItemDisplayText);

        bookingsDisplay.setAdapter(adapterForBookings);
    }

    public void completeBookings(View view) {
        int counter = 0;
        for (Iterator<Booking> iterator = allBookings.iterator(); iterator.hasNext();) {
            Booking booking = iterator.next();
            String curEmail = listOfBookingSeekerEmails.get(counter);
            String curIdentifier = listOfBookingIdentifiers.get(counter);
            Date endTime = myCalendarToDate(booking.getEndCalendarDate());
            Date currTime = new Date();

            System.out.println("curr: " + currTime + " endTime: " + endTime);

            if (currTime.after(endTime)) {
                Global.spaces().child(Global.reformatEmail(ownerEmail)).child(spaceName).child("currentBookingIdentifiers").child(curIdentifier).removeValue();
                Global.bookings().child(Global.reformatEmail(curEmail)).child(curIdentifier).child("done").setValue(true);
                iterator.remove();
            }
        }
        startActivity(new Intent(FinishBookingsActivity.this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    private Date myCalendarToDate(MyCalendar calendar){
        String year, month, day, hour, minute;
        Date dateTime;

        year = String.valueOf(calendar.getYear()).substring(2);
        month = getDoubleDigit(calendar.getMonth());
        day = getDoubleDigit(calendar.getDay());
        hour = getDoubleDigit(calendar.getHour());
        minute = getDoubleDigit(calendar.getMinute());
        String sdt = year + month + day + hour + minute + "00-0700";
        try {
            dateTime = format.parse(sdt);
            return  dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDoubleDigit(int i){
        String result;
        if(i < 10){
            result = "0" + i;
        }
        else{
            result = "" + i;
        }

        return result;
    }
}
