package com.pfp.parkhere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.LinkedList;

import ObjectClasses.Space;
import ObjectClasses.SpaceType;


public class MyListedSpacesActivity extends AppCompatActivity {

    public static String LISTING_SPACE_MESSAGE = "com.pfp.parkhere.LISTINGSPACEMESSAGE";
    ListView listedSpacesList;
    LinkedList<Space> MyListedSpaces;
    String [] strFormattedSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces);

        listedSpacesList = (ListView) findViewById(R.id.listed_spaces_list);
        MyListedSpaces = new LinkedList<Space>();


    }

    private LinkedList<Space> createTestSpaces() {
        LinkedList<Space> retList = new LinkedList<Space>();

        for (int i = 1; i <= 5; i++) {
            Space testSpace = new Space();
            testSpace.setPricePerHour(5*i);
            testSpace.setSpaceName("My Test Space" + i);
            testSpace.setType(SpaceType.TRUCK);

        }

        return retList;
    }
}
