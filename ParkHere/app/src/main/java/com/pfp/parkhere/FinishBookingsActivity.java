package com.pfp.parkhere;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;
import ObjectClasses.Space;

//TODO Finish integrating this class
//TODO Firebase - Add field to Peer: available balance
//TODO Firebase - Add field to Space: list of Booking
//TODO Fireabse - Remove field from Booking: done
//TODO Add fields for review and ratings
//TODO Create a function to get average of all ratings
//TODO Append new review to a list of string reviews
//TODO Finish booking activity doesn't check owner status, doesn't check owner owns this.
public class FinishBookingsActivity extends AppCompatActivity {

    private ArrayList<Booking> bookingsForSpace;
    private SimpleDateFormat format = new SimpleDateFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_bookings);

        bookingsForSpace = generateBookings();
        updateDisplay();
    }

    private void updateDisplay() {

        TextView nameOfSpace = (TextView) findViewById(R.id.space_name_for_bookings);
        if (bookingsForSpace.size() != 0) {
            nameOfSpace.setText("Showing bookings for " +
                    bookingsForSpace.get(0).getSpaceName());
        }
        else {
            nameOfSpace.setText("This space has no active bookings.");
        }

        ListView bookingsDisplay = (ListView)findViewById(R.id.list_of_bookings);

        String [] strFormattedBookings = new String[bookingsForSpace.size()];

        for (int i = 0; i < bookingsForSpace.size(); i++) {
            Booking toFormat = bookingsForSpace.get(i);
            MyCalendar startTime = toFormat.getStartCalendarDate();
            MyCalendar endTime = toFormat.getEndCalendarDate();
            strFormattedBookings[i] = toFormat.getSpaceName() + "\n"
                    + startTime.getMonth() + "/"
                    + startTime.getDay() + "/"
                    + startTime.getYear() + " "
                    + String.format("%02d", startTime.getHour()) +":"
                    + String.format("%02d", startTime.getMinute()) + " - "
                    + String.format("%02d", endTime.getHour()) + ":"
                    + String.format("%02d", endTime.getMinute());
        }

        ArrayAdapter<String> adapterForBookings = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, strFormattedBookings);

        bookingsDisplay.setAdapter(adapterForBookings);
    }

    private ArrayList<Booking> generateBookings () {
        ArrayList<Booking> retVal = new ArrayList<Booking>();

        for (int i = 0; i < 11; i++) {
            Booking bookingToAdd = new Booking();
            bookingToAdd.setSpaceName("My Test Space");

            MyCalendar startTime = new MyCalendar(2016, 11, 4, i+1, 0);
            MyCalendar endTime = new MyCalendar(2016, 11, 4, i+2, 0);

            bookingToAdd.setStartCalendarDate(startTime);
            bookingToAdd.setEndCalendarDate(endTime);

            retVal.add(bookingToAdd);
        }

        return retVal;
    }

    public void completeBookings(View view) {
        int countOfRemovedSpaces = 0;
        List<Booking> newBookings = new LinkedList<>();
        for (int i = 0; i < bookingsForSpace.size(); i++) {
            Booking currBooking = bookingsForSpace.get(i);
            Date endTime = myCalendarToDate(currBooking.getEndCalendarDate());
            Date currTime = new Date();

            if (currTime.after(endTime)) {
                newBookings.add(currBooking);
//                bookingsForSpace.remove(i-countOfRemovedSpaces);
                countOfRemovedSpaces++;
            }

        }

        //TODO Delete Booking from firebase

        bookingsForSpace = new ArrayList<>(newBookings);

        AlertDialog dialog;
        AlertDialog.Builder completedDisplay = new AlertDialog.Builder(view.getContext());
        completedDisplay.setTitle("Success!")
                .setMessage("Finished " + countOfRemovedSpaces + " bookings.");
        completedDisplay.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDisplay();
                dialog.dismiss();
            }
        });

        dialog = completedDisplay.create();
        dialog.show();

    }

    private Date myCalendarToDate(MyCalendar calendar){
        String year, month, day, hour, minute;
        Date dateTime;

        year = String.valueOf(calendar.getYear());
        month = getDoubleDigit(calendar.getMonth());
        day = getDoubleDigit(calendar.getDay());
        hour = getDoubleDigit(calendar.getHour());
        minute = getDoubleDigit(calendar.getMinute());
        String sdt = year + "."
                + month + "." + day + "." + hour + "." + minute + ".00";
        System.out.println(sdt);
        try {
            dateTime = format.parse(sdt);
            return  dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDoubleDigit(int i){
        String result = "";
        if(i < 10){
            result = "0" + i;
        }
        else{
            result = "" + i;
        }

        return result;
    }
}
