package com.pfp.parkhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.LinkedList;

import ObjectClasses.Booking;
import ObjectClasses.Peer;
import ObjectClasses.Space;

public class MyBookingsActivity extends Activity {
    private ListView bookingsView;
    private LinkedList<String> myBookingIdentifiers;
    private LinkedList<Booking> myBookings;
    private String[] viewValues;
    private int ownerRating;
    private Intent intent;
    private Bundle extras;
    private Booking currBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        //get bookings list
        bookingsView = (ListView) findViewById(R.id.bookinglist);
        myBookingIdentifiers = new LinkedList<>();
        myBookings = new LinkedList<>();

        Global.bookings().child(Global.getCurUser().getReformattedEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    myBookingIdentifiers.add(postSnapshot.getKey());
                    myBookings.add(postSnapshot.getValue(Booking.class));
                }
                populate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void populate() {
        //Set values displayed as a list
        viewValues = new String[myBookings.size()];
        for (int i = 0; i < myBookings.size(); i++) {
            viewValues[i] = myBookings.get(i).getSpaceName();
        }

        //Adapter to dynamically fill listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, viewValues);

        // Assign adapter to ListView
        bookingsView.setAdapter(adapter);

        // ListView Item Click Listener
        bookingsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bookingClicked(view, position);
            }
        });
    }

    private void bookingClicked(View view, final int position){
        // ListView Clicked item index
        Context context = view.getContext();

        //New intent to pass to booking details, new Bundle to attach to intent
        intent = new Intent(context, MyBookingsDetailsActivity.class);
        extras = new Bundle();

        currBooking = myBookings.get(position);

        Global.spaces().child(Global.reformatEmail(currBooking.getBookingSpaceOwnerEmail()))
                .child(currBooking.getSpaceName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookingClicked1(dataSnapshot.getValue(Space.class), myBookingIdentifiers.get(position));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bookingClicked1(Space space, String bookingIdentifier){
        //Generate text for address
        String ad = space.getStreetAddress()
                + " " + space.getCity() + " " + space.getState()
                + " " + space.getZipCode();
        Address bookingAddress = null;
        try {
            bookingAddress = new Geocoder(MyBookingsActivity.this).getFromLocationName(ad, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String addressText = bookingAddress.getAddressLine(0) + "\n" +
                bookingAddress.getLocality() + " " + bookingAddress.getAdminArea();
        extras.putString("SPACE_ADDRESS", addressText);

        //Generate text for start and end dates
        String startMinute;
        int sMinuteInt = currBooking.getStartCalendarDate().getMinute();
        if(sMinuteInt < 10){
            startMinute = "0" + sMinuteInt;
        }
        else{
            startMinute = sMinuteInt + "";
        }
        String endMinute;
        int eMinuteInt = currBooking.getEndCalendarDate().getMinute();
        if(eMinuteInt < 10){
            endMinute = "0" + eMinuteInt;
        }
        else{
            endMinute = eMinuteInt + "";
        }
        String endTimeText = currBooking.getEndCalendarDate().getHour() + ":"
                + startMinute + " "
                + currBooking.getEndCalendarDate().getMonth() + "/"
                + currBooking.getEndCalendarDate().getDay() + "/"
                + currBooking.getEndCalendarDate().getYear();
        String startTimeText = currBooking.getStartCalendarDate().getHour() + ":"
                + endMinute + " "
                + currBooking.getStartCalendarDate().getMonth()+ "/"
                + currBooking.getStartCalendarDate().getDay() + "/"
                + currBooking.getStartCalendarDate().getYear();
        extras.putString("BOOKING_STARTTIME", startTimeText);
        extras.putString("BOOKING_ENDTIME", endTimeText);
        //Generate text for owner name and email
        extras.putString("SPACE_OWNERNAME", space.getSpaceName());
        extras.putString("SPACE_OWNEREMAIL", space.getOwnerEmail());
        extras.putString("SPACE_NAME", space.getSpaceName());
        extras.putString("BOOKING_IDENTIFIER", bookingIdentifier);
        extras.putInt("SPACE_RATING", ownerRating);
        extras.putBoolean("BOOKING_DONE", currBooking.isDone());

        //Generate Rating and Review
        //Get owner object to set rating
        String bookingsSpacesOwnerEmail = space.getOwnerEmail();
        Global.peers().child(Global.reformatEmail(bookingsSpacesOwnerEmail)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Peer currentUser = dataSnapshot.getValue(Peer.class);
                ownerRating = ((Peer) currentUser).getOwnerRating();
                bookingClicked2(extras);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void bookingClicked2(Bundle extras){
        intent.putExtras(extras);
        startActivity(intent);
    }

    //Used to create hard coded bookings
//    private void mockBookings(LinkedList<Booking> myBookings) {
//        for(int i = 0; i < 11;i++){
//            //create new booking
//            Booking tempBooking = new Booking();
//            //start and end times for booking
//            tempBooking.setStart(new GregorianCalendar(2000+i,1+i,1+i,3+i,1+i));
//            tempBooking.setEnd(new GregorianCalendar(2000+i,1+i,1+i,4+i,1+i));
//            //new space
//            Space tempSpace = new Space();
//            //set address in space
//            tempSpace.setStreetAddress("1 Infinity LoopCupertinoCA");
//            tempSpace.setOwnerEmail("ownerName" + i + "@email.net");
//            //set owner name
//            tempSpace.setSpaceName("FirstName LastName");
//            //set review and rating
//            tempSpace.setSpaceReview("This space got my car towed.");
////            tempSpace.setOwnerRating(3); No need. Owner rating will be retrieved from owner email - owner object
//            //put space in boooking and add booking to linked list
//            tempBooking.setSpace(tempSpace);
//            myBookings.add(tempBooking);
//        }
//    }


}