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
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.LinkedList;

import ObjectClasses.Space;
import ObjectClasses.SpaceType;


public class MyListedSpacesActivity extends AppCompatActivity {

    public static String LISTING_SPACE_MESSAGE = "com.pfp.parkhere.LISTINGSPACEMESSAGE";
    ListView listedSpacesList;
    LinkedList<Space> MyListedSpaces;
    String [] strFormattedSpaces;

    //TODO FinishBookingActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listed_spaces);

        //Retrieve the list to add the new elements to
        listedSpacesList = (ListView) findViewById(R.id.listed_spaces_list);
        MyListedSpaces = new LinkedList<Space>();

        String currentUserEmail = Global_ParkHere_Application.getCurrentUserObject().getEmailAddress();
        //Retrieve all the current user's actual space objects
        FirebaseDatabase.getInstance().getReference().child("Spaces")
                .child(Global_ParkHere_Application.reformatEmail(currentUserEmail))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            MyListedSpaces.add(postSnapshot.getValue(Space.class));
                        }
                        populate();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void populate(){
        //Format the information into an array of strings to add to the list
        strFormattedSpaces = new String[MyListedSpaces.size()];
        for (int i = 0; i < MyListedSpaces.size(); i++) {
            Space currSpace = MyListedSpaces.get(i);
            String ad = currSpace.getStreetAddress();
            Address currAddress = null;
            try {
                currAddress = new Geocoder(this).getFromLocationName(ad, 1).get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            strFormattedSpaces[i] = currSpace.getSpaceName() + "\n" +
                    currAddress.getAddressLine(0) + ",\n" +
                    currAddress.getLocality() + ", " +
                    currAddress.getAdminArea();
        }

        //This adapts the strings to be compatable with the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, strFormattedSpaces);

        listedSpacesList.setAdapter(arrayAdapter);
        listedSpacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                Intent intent = new Intent(context, MyListedSpacesDetailsActivity.class);
                Bundle extras = new Bundle();
                Space chosenSpace = MyListedSpaces.get(position);
                extras.putString("LISTED_SPACE_NAME", chosenSpace.getSpaceName());
                extras.putInt("LISTED_SPACE_PRICE", chosenSpace.getPricePerHour());
                extras.putString("LISTED_SPACE_ADDRESS", chosenSpace.getStreetAddress());
                extras.putString("LISTED_SPACE_CITY", chosenSpace.getCity());
                extras.putString("LISTED_SPACE_STATE", chosenSpace.getState());
                extras.putString("LISTED_SPACE_ZIP", chosenSpace.getZipCode());
                extras.putString("LISTED_SPACE_TYPE", String.valueOf(chosenSpace.getType()));
                extras.putString("LISTED_SPACE_POLICY", String.valueOf(chosenSpace.getPolicy()));
                extras.putString("LISTED_SPACE_DESCRIPTION", chosenSpace.getDescription());
                extras.putInt("LISTED_SPACE_RATING", chosenSpace.getSpaceRating());
                extras.putString("LISTED_SPACE_REVIEW", chosenSpace.getSpaceReview());
                extras.putString("LISTED_SPACE_OWNEREMAIL", chosenSpace.getOwnerEmail());

                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
    }

    //Used to create hard coded spaces
//    private LinkedList<Space> createTestSpaces() {
//        LinkedList<Space> retList = new LinkedList<Space>();
//
//        for (int i = 1; i <= 11; i++) {
//            Space testSpace = new Space();
//
//            testSpace.setSpaceName("My Test Space "+ i);
//            testSpace.setPricePerHour(5*i);
//            testSpace.setType(SpaceType.TRUCK);
//
//            testSpace.setStreetAddress("654" + i + " Washington Avenue");
//            testSpace.setCity("Los Angeles");
//            testSpace.setState("CA");
//            testSpace.setZipCode("90007");
//
//            retList.add(testSpace);
//        }
//
//        return retList;
//    }


}
