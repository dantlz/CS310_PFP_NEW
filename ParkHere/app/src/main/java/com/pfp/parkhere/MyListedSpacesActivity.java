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

import java.util.LinkedList;
import java.util.Locale;

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

        //Retrieve the list to add the new elements to
        listedSpacesList = (ListView) findViewById(R.id.listed_spaces_list);
        MyListedSpaces = new LinkedList<Space>();

        //Generate test cases for spaces
        MyListedSpaces = createTestSpaces();

        //Format the information into an array of strings to add to the list
        strFormattedSpaces = new String[MyListedSpaces.size()];
        for (int i = 0; i < MyListedSpaces.size(); i++) {
            Space currSpace = MyListedSpaces.get(i);
            Address currAddress = currSpace.getAddress();
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

                extras.putDouble("LISTED_SPACE_PRICE", chosenSpace.getPricePerHour());

                Address spaceAddress = chosenSpace.getAddress();
                extras.putString("LISTED_SPACE_ADDRESS", spaceAddress.getAddressLine(0)+ ",\n" +
                            spaceAddress.getLocality() + ", "
                            + spaceAddress.getAdminArea() + " " + spaceAddress.getPostalCode());

                intent.putExtras(extras);

                context.startActivity(intent);
            }
        });
    }

    private LinkedList<Space> createTestSpaces() {
        LinkedList<Space> retList = new LinkedList<Space>();

        for (int i = 1; i <= 11; i++) {
            Space testSpace = new Space();

            testSpace.setSpaceName("My Test Space "+ i);
            testSpace.setPricePerHour(5*i);
            testSpace.setType(SpaceType.TRUCK);

            Address testAddress = new Address(Locale.ENGLISH);
            testAddress.setAddressLine(0, "654" + i + " Washington Avenue");
            testAddress.setLocality("Los Angeles");
            testAddress.setAdminArea("CA");
            testAddress.setPostalCode("90007");

            testSpace.setAddress(testAddress);

            retList.add(testSpace);
        }

        return retList;
    }
}
