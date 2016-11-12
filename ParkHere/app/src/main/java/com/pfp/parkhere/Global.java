package com.pfp.parkhere;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ObjectClasses.CancellationPolicy;
import ObjectClasses.MyCalendar;
import ObjectClasses.Peer;
import ObjectClasses.Space;

public class Global extends Application {


    private static HashMap<LatLng, Space> mapOfLatLngSpacesToPass;
    private static Peer currentUserObject;

    //To ensure the dates are not null
    private static MyCalendar currentSearchTimeDateStart = new MyCalendar();
    private static MyCalendar currentSearchTimedateEnd = new MyCalendar();

    public static MyCalendar getCurrentSearchTimeDateStart() {
        return currentSearchTimeDateStart;
    }

    public static void setCurrentSearchTimeDateStart(MyCalendar searchStart) {
        currentSearchTimeDateStart = searchStart;
    }

    public static MyCalendar getCurrentSearchTimedateEnd() {
        return currentSearchTimedateEnd;
    }

    public static void setCurrentSearchTimedateEnd(MyCalendar searchEnd) {
        currentSearchTimedateEnd = searchEnd;
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

    public static Peer getCurUser() {
        return currentUserObject;
    }

    public static void setCurUser(Peer obj) {
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

    public static HashMap<LatLng, Space> getMapOfLatLngSpacesToPass() {
        return mapOfLatLngSpacesToPass;
    }

    public static void setMapOfLatLngSpacesToPass(HashMap<LatLng, Space> mapOfLatLngSpacesToPass) {
        Global.mapOfLatLngSpacesToPass = mapOfLatLngSpacesToPass;
    }

    public static DatabaseReference peers(){
        return FirebaseDatabase.getInstance().getReference().child("Peers");
    }

    public static DatabaseReference spaces(){
        return FirebaseDatabase.getInstance().getReference().child("Spaces");
    }

    public static DatabaseReference bookings(){
        return FirebaseDatabase.getInstance().getReference().child("Bookings");
    }

    public static DatabaseReference spaceReviews(){
        return FirebaseDatabase.getInstance().getReference().child("SpaceReviews");
    }

    public static DatabaseReference ownerReviews(){
        return FirebaseDatabase.getInstance().getReference().child("OwnerReviews");
    }

    public static DatabaseReference curUserRef(){
        return Global.peers().child(Global.getCurUser().getReformattedEmail());
    }
}
