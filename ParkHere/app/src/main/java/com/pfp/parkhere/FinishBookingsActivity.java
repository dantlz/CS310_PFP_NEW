package com.pfp.parkhere;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;
import ObjectClasses.Space;

public class FinishBookingsActivity extends AppCompatActivity {

    private ArrayList<Booking> bookingsForSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_bookings);

        bookingsForSpace = generateBookings();

        System.out.println(bookingsForSpace.size());

        updateDisplay();
    }

    private void updateDisplay() {

        TextView nameOfSpace = (TextView) findViewById(R.id.space_name_for_bookings);
        if (bookingsForSpace.size() != 0) {
            nameOfSpace.setText("Showing bookings for " +
                    bookingsForSpace.get(0).getSpace().getSpaceName());
        }
        else {
            nameOfSpace.setText("This space has no active bookings.");
        }

        ListView bookingsDisplay = (ListView)findViewById(R.id.list_of_bookings);

        String [] strFormattedBookings = new String[bookingsForSpace.size()];

        for (int i = 0; i < bookingsForSpace.size(); i++) {
            Booking toFormat = bookingsForSpace.get(i);
            MyCalendar startTime = toFormat.getStart();
            MyCalendar endTime = toFormat.getEnd();
            strFormattedBookings[i] = toFormat.getSpace().getSpaceName() + "\n"
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
            Space spaceOfBooking = new Space();
            spaceOfBooking.setSpaceName("My Test Space");

            Booking bookingToAdd = new Booking();
            bookingToAdd.setSpace(spaceOfBooking);

            MyCalendar startTime = new MyCalendar(2016, 11, 4, i+1, 0);
            MyCalendar endTime = new MyCalendar(2016, 11, 4, i+2, 0);

            bookingToAdd.setStart(startTime);
            bookingToAdd.setEnd(endTime);

            retVal.add(bookingToAdd);
        }

        return retVal;
    }

    public void completeBookings(View view) {
        int countOfRemovedSpaces = 0;
        for (int i = 0; i < bookingsForSpace.size(); i++) {
            Booking currBooking = bookingsForSpace.get(i);
            MyCalendar reformEndTime = currBooking.getEnd();
            GregorianCalendar endTime = new GregorianCalendar(reformEndTime.getYear(),
                        reformEndTime.getMonth(), reformEndTime.getDay(), reformEndTime.getHour(),
                        reformEndTime.getMinute());
            Calendar currTime = Calendar.getInstance();

            if (!currTime.before(endTime)) {
                bookingsForSpace.remove(i);
                countOfRemovedSpaces++;
            }

        }

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
}
