package com.pfp.parkhere;

import android.app.Application;

import ObjectClasses.Peer;

/**
 * Created by tianlinz on 10/19/16.
 */

public class Global_ParkHere_Application extends Application {

    private Peer currentUserObject;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Peer getCurrentUserObject() {
        return currentUserObject;
    }

    public void setCurrentUserObject(Peer currentUserObject) {
        this.currentUserObject = currentUserObject;
    }

}
