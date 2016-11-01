package com.pfp.parkhere;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
import ObjectClasses.Peer;

public class Global_ParkHere_Application extends Application {

    private static Peer currentUserObject;
    //To ensure the dates are not null
    private static MyCalendar currentSearchTimeDateStart = new MyCalendar();
    private static MyCalendar currentSearchTimedateEnd = new MyCalendar();

    public static MyCalendar getCurrentSearchTimeDateStart() {
        return currentSearchTimeDateStart;
    }

    public static void setCurrentSearchTimeDateStart(MyCalendar currentSearchTimeDateStart) {
        currentSearchTimeDateStart = currentSearchTimeDateStart;
    }

    public static MyCalendar getCurrentSearchTimedateEnd() {
        return currentSearchTimedateEnd;
    }

    public static void setCurrentSearchTimedateEnd(MyCalendar currentSearchTimedateEnd) {
        currentSearchTimedateEnd = currentSearchTimedateEnd;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String getCancellationPolicy(CancellationPolicy policy) {
        if (policy == CancellationPolicy.LIGHT) {
            return "Flexible: You will receive a full refund if you cancel up to 24 hours in advance.";
        }
        else if (policy == CancellationPolicy.MODERATE) {
            return "Moderate: You will receive a 50% refund if you cancel up to 24 hours in advance.";
        }
        else {
            return "Strict: You will not receive a refund if you cancel your reservation.";
        }
    }

    public static void addListener(){
        FirebaseDatabase.getInstance().getReference().child("Peers")
                .child(Global_ParkHere_Application.reformatEmail(currentUserObject.getEmailAddress()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        setCurrentUserObject(dataSnapshot.getValue(Peer.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static Peer getCurrentUserObject() {
        return currentUserObject;
    }

    public static void setCurrentUserObject(Peer obj) {
        currentUserObject = obj;
    }

    public static String reformatEmail(String email){
        String reformattedEmail = email;
        reformattedEmail = reformattedEmail.replace(".", "");
        reformattedEmail = reformattedEmail.replace("#", "");
        reformattedEmail = reformattedEmail.replace("$", "");
        reformattedEmail = reformattedEmail.replace("[", "");
        reformattedEmail = reformattedEmail.replace("]", "");
        return reformattedEmail;
    }

}
