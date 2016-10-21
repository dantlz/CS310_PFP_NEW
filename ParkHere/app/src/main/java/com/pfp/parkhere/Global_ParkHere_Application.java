package com.pfp.parkhere;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

import ObjectClasses.Peer;

/**
 * Created by tianlinz on 10/19/16.
 */

public class Global_ParkHere_Application extends Application {

    private boolean isAuthenticated;
    private Peer currentUser;
//    private FirebaseUser ref, refCurrentUser;

    @Override
    public void onCreate() {
        super.onCreate();
//        ref =  new Firebase("https://anchronize.firebaseio.com");
        isAuthenticated = false;    //default it to false when it's created first
    }

    public Peer getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Peer currentUser) {
        this.currentUser = currentUser;
    }

    public void setAuthenticateStatus(boolean authStatus) {
        this.isAuthenticated = authStatus;
    }

    public boolean getAuthenticateStatus() {
        return isAuthenticated;
    }
}
