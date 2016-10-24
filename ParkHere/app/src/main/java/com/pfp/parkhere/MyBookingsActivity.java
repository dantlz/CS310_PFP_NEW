package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;

import ObjectClasses.Booking;
import ObjectClasses.Space;


public class MyBookingsActivity extends AppCompatActivity {
    public final static String BOOKING_DETAIL_MESSAGE = "com.pfp.parkhere.BOOKINGDETAILMESSAGE";
    ListView bookingsView;
    LinkedList<Booking> myBookingsTest;
    //GregorianCalendar[] dateValues;
    String[] viewValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        //get bookings list
        bookingsView = (ListView) findViewById(R.id.bookinglist);
        //get test bookings
        myBookingsTest = new LinkedList<Booking>();
        mockBookings(myBookingsTest);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //Set values displayed as a list
        viewValues = new String[myBookingsTest.size()];
        for(int i = 0; i < myBookingsTest.size();i++){
            GregorianCalendar tempDate = myBookingsTest.get(i).getStart();
            viewValues[i] = timeFormat.format(tempDate.getTime())+ " "
                + tempDate.getDisplayName(Calendar.AM_PM,Calendar.SHORT,Locale.ENGLISH)+ " " +
                    tempDate.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.ENGLISH) + " " +
                    tempDate.get(Calendar.DAY_OF_MONTH);
        }

        //Adapter to dynamically fill listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);

        // Assign adapter to ListView
        bookingsView.setAdapter(adapter);

        // ListView Item Click Listener
        bookingsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                // ListView Clicked item index
                int itemPosition  = position;
                Context context = view.getContext();
                Intent intent = new Intent(context, MyBookingsDetailsActivity.class);
                GregorianCalendar endTime = myBookingsTest.get(position).getEnd();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String message = timeFormat.format(endTime.getTime())+ " "
                        + endTime.getDisplayName(Calendar.AM_PM,Calendar.SHORT,Locale.ENGLISH)+ " " +
                        endTime.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.ENGLISH) + " " +
                        endTime.get(Calendar.DAY_OF_MONTH);
                intent.putExtra(BOOKING_DETAIL_MESSAGE, message);
                startActivity(intent);
            }

        });
    }
    private void mockBookings(LinkedList<Booking> myBookingsTest) {
        for(int i = 0; i < 11;i++){
            //create new booking
            Booking tempBooking = new Booking();
            //start and end times for booking
            tempBooking.setStart(new GregorianCalendar(2000+i,1+i,1+i,3+i,1+i));
            tempBooking.setEnd(new GregorianCalendar(2000+i,1+i,1+i,4+i,1+i));
            //new space
            Space tempSpace = new Space();
            //new address for space
            Address tempAddress = new Address(Locale.ENGLISH);
            tempAddress.setAddressLine(0,"1 Infinity Loop");
            tempAddress.setLocality("Cupertino");
            tempAddress.setAdminArea("CA");
            //set address in space
            tempSpace.setAddress(tempAddress);
            tempSpace.setOwnerEmail("ownerNumber" + i + "@email.net");
            tempSpace.setName("Booked Space");
            tempBooking.setSpace(tempSpace);
            myBookingsTest.add(tempBooking);
        }
    }

}