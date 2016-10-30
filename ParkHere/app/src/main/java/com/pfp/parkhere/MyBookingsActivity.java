package com.pfp.parkhere;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;

import ObjectClasses.Booking;
import ObjectClasses.MyCalendar;
import ObjectClasses.Peer;
import ObjectClasses.Space;


public class MyBookingsActivity extends AppCompatActivity {
    public final static String BOOKING_DETAIL_MESSAGE = "com.pfp.parkhere.BOOKINGDETAILMESSAGE";
    ListView bookingsView;
    LinkedList<String> myBookingIdentifiers;
    LinkedList<Booking> myBookings;
    String[] viewValues;
    int ownerRating;
    Intent intent;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        //get bookings list
        bookingsView = (ListView) findViewById(R.id.bookinglist);

        //HARD CODED BOOKINGs
        //get test bookings, in the future requests all the bookings from database
        myBookings = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference().child("Bookings")
                .child(
                        Global_ParkHere_Application.reformatEmail(
                                Global_ParkHere_Application.getCurrentUserObject().getEmailAddress()))
                .addValueEventListener(new ValueEventListener() {
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
            MyCalendar tempDate = myBookings.get(i).getStart();
            viewValues[i] = tempDate.getHour() + " " + tempDate.getMinute() + " "
                    +tempDate.getDay() + " " + tempDate.getMonth() + " " + tempDate.getYear();
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

                // ListView Clicked item index
                Context context = view.getContext();

                //New intent to pass to booking details, new Bundle to attach to intent
                intent = new Intent(context, MyBookingsDetailsActivity.class);
                extras = new Bundle();

                Booking currBooking = myBookings.get(position);

                //Generate text for address
                String ad = currBooking.getSpace().getStreetAddress()
                + " " + currBooking.getSpace().getCity() + " " + currBooking.getSpace().getState()
                        + " " + currBooking.getSpace().getZipCode();
                Address bookingAddress = null;
                try {
                    bookingAddress = new Geocoder(MyBookingsActivity.this).getFromLocationName(ad, 1).get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String addressText = bookingAddress.getAddressLine(0) + "\n" +
                        bookingAddress.getLocality() + " " + bookingAddress.getAdminArea();
                extras.putString("ADDRESS_TEXT", addressText);

                //Generate text for start and end dates
                String endTimeText = currBooking.getEnd().getHour() + " "
                        + currBooking.getEnd().getMinute() + " "
                        +currBooking.getEnd().getDay() + " " + currBooking.getEnd().getMonth()
                        + " " + currBooking.getEnd().getYear();
                String startTimeText = currBooking.getStart().getHour() + " "
                        + currBooking.getStart().getMinute() + " "
                        +currBooking.getStart().getDay() + " " + currBooking.getStart().getMonth()
                        + " " + currBooking.getStart().getYear();
                extras.putString("START_TIME_TEXT", startTimeText);
                extras.putString("END_TIME_TEXT", endTimeText);
                //Generate text for owner name and email
                extras.putString("OWNER_NAME_TEXT", myBookings.get(position).getSpace().getSpaceName());
                extras.putString("OWNER_EMAIL_TEXT", myBookings.get(position).getSpace().getOwnerEmail());
                extras.putString("SPACE_REVIEW_TEXT", myBookings.get(position).getSpace().getSpaceReview());
                extras.putSerializable("IDENTIFIER", myBookingIdentifiers.get(position));

                //Generate Rating and Review
                //Get owner object to set rating
                String bookingsSpacesOwnerEmail = myBookings.get(position).getSpace().getOwnerEmail();
                FirebaseDatabase.getInstance()
                        .getReference("Peers")
                        .child(Global_ParkHere_Application.reformatEmail(bookingsSpacesOwnerEmail))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.

                                Peer currentUser = dataSnapshot.getValue(Peer.class);
                                ownerRating = ((Peer) currentUser).getOwnerRating();
                                finishPopulate(extras);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });

            }
        });
    }

    private void finishPopulate(Bundle extras){
        extras.putString("SPACE_RATING_TEXT", ownerRating + "");

        //Place bundle into intent and start activity
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


    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().signOut();
        super.onStop();
    }
}