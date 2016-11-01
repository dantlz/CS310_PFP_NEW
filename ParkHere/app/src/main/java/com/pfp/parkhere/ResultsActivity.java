package com.pfp.parkhere;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import ObjectClasses.Status;

public class ResultsActivity extends AppCompatActivity
{

    private Map<LatLng, ObjectClasses.Space> resultsMap;
    private LatLng userAddress;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> viewValues;
    private ListView list;
    private ArrayList<ObjectClasses.Space> spaceList;
    private ArrayList<Integer> priceList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //TODO: Comment these back in when
        Bundle extra = getIntent().getExtras();
        userAddress = (LatLng)extra.get("LATLNG");
        resultsMap = Global_ParkHere_Application.getMapOfLatLngSpacesToPass();

        list = (ListView) findViewById(R.id.resultsListView);
        viewValues = new ArrayList<String>();
        spaceList = new ArrayList<ObjectClasses.Space>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, viewValues);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int n = position;
                ObjectClasses.Space spx = spaceList.get(n);
                Intent intent = new Intent(ResultsActivity.this, MyListedSpacesDetailsActivity.class);
                intent.putExtra("SPACENAME", spx.getSpaceName());
                intent.putExtra("OWNEREMAIL", spx.getOwnerEmail());
                intent.putExtra("STATUS", String.valueOf(Status.SEEKER));

                startActivity(intent);
            }
        });

        populateListView();
    }

    private LatLng latLngFromAddress(String fullAddress){
        try {
            Address address = new Geocoder(this).getFromLocationName(fullAddress, 1).get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void populateListView()
    {
        for (Map.Entry<LatLng, ObjectClasses.Space> spaceEntry : resultsMap.entrySet())
        {
            LatLng latlng = (LatLng) spaceEntry.getKey(); //LatLng Object
            ObjectClasses.Space spacex = spaceEntry.getValue(); //Space Object
            addItems(spacex.getOwnerEmail() + " $" + spacex.getPricePerHour() + " - " + (int)calculateDistanceFromTo(latlng, userAddress) + " miles");
            spaceList.add(spacex);

            //POPULATE PriceList using the spaceMap
            priceList.add(spacex.getPricePerHour());
        }
    }

    public void addItems(String x)
    {
        viewValues.add(x);
        adapter.notifyDataSetChanged();
    }

//    public void populateMapDummy()
//    {
//
//        String dtla = "DTLA";
//        String usc = "University of Southern California";
//        String coliseum = "USC Coliseum";
//        List<Address> addressList = null;
//        Geocoder geocoder;
//        if (dtla != null || !dtla.equals(""))
//        {
//
//            //DTLA
//            geocoder = new Geocoder(this);
//            try
//            {
//                addressList = geocoder.getFromLocationName(dtla, 1);
//                if(!addressList.isEmpty())
//                {
//                    Address address = addressList.get(0);
//                    LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
//                    ObjectClasses.Space dtlaspace1 = new ObjectClasses.Space();
//                    dtlaspace1.setOwnerEmail("Lorenzo@zo.com");
//                    dtlaspace1.setPricePerHour(5);
//                    dtlaspace1.setSpaceName("DTLA");
//                    resultsMap.put(latLng1, dtlaspace1);
//                }
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
//            //USC
//            geocoder = new Geocoder(this);
//            try
//            {
//                addressList = geocoder.getFromLocationName(usc, 1);
//                if(!addressList.isEmpty())
//                {
//
//                    Address address = addressList.get(0);
//                    LatLng latLng2 = new LatLng(address.getLatitude(), address.getLongitude());
//                    ObjectClasses.Space dtlaspace2 = new ObjectClasses.Space();
//                    dtlaspace2.setOwnerEmail("tommytrojan.usc.edu");
//                    dtlaspace2.setPricePerHour(10);
//                    dtlaspace2.setSpaceName("USC");
//                    resultsMap.put(latLng2, dtlaspace2);
//
//                }
//
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
//            //Coliseum
//            geocoder = new Geocoder(this);
//            try
//            {
//
//                addressList = geocoder.getFromLocationName(coliseum, 1);
//                if(!addressList.isEmpty())
//                {
//                    Address address = addressList.get(0);
//                    LatLng latLng3 = new LatLng(address.getLatitude(), address.getLongitude());
//                    ObjectClasses.Space dtlaspace3 = new ObjectClasses.Space();
//                    dtlaspace3.setOwnerEmail("nikiasxxx.usc.edu");
//                    dtlaspace3.setPricePerHour(20);
//                    dtlaspace3.setSpaceName("Coliseum");
//                    resultsMap.put(latLng3, dtlaspace3);
//                }
//            }
//
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
//
//
//        }
//
//
//    }

    public void sortByPrice(View view)
    {
        Collections.sort(viewValues, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                Integer dollapos1 = o1.indexOf('$');
                Integer dollapos2 = o2.indexOf('$');
                String s1p = "";
                String s2p = "";
                for(int i=dollapos1+1; i<o1.indexOf('-')-1; i++)
                {
                    s1p+=o1.charAt(i);
                }

                for(int i=dollapos2+1; i<o2.indexOf('-')-1; i++)
                {
                    s2p+=o2.charAt(i);
                }

                Integer x = Integer.parseInt(s1p);
                Integer y = Integer.parseInt(s2p);

                if(x==y)
                {
                    return 0;
                }
                else if(x<y)
                {
                    return -1;
                }
                else if(x>y)
                {
                    return 1;
                }
                return 0;
            }
        });

        sortSpaceList(0);
        adapter.notifyDataSetChanged();
    }

    //TODO Optimizie the sort distance speed
    public void sortByDistance(View view) {
//        //1 = distanceSort the spaceList
//        sortSpaceList(1);
//        ArrayList<String> newViewValues = new ArrayList<String>();
//        for(int i =0; i<spaceList.size(); i++)
//        {
//            for(int j=0; j<viewValues.size(); j++)
//            {
//                String email = "";
//                String vv = viewValues.get(j);
//                int toindex = vv.indexOf('$')-1;
//                email = vv.substring(0, toindex);
//                if(email.matches(spaceList.get(i).getOwnerEmail()))
//                {
//                    newViewValues.add(vv);
//                }
//            }
//        }
//
//
//
//        viewValues = new ArrayList<>(newViewValues);
//        adapter.notifyDataSetChanged();

        Collections.sort(viewValues, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
//                addItems(spacex.getOwnerEmail() + " $" + spacex.getPricePerHour() + " - " + (int)calculateDistanceFromTo(latlng, userAddress) + " miles");
                //"emal $5 - one after the hyphen
                String distance1 = "";
                String distance2 = "";
                distance1 = o1.substring(o1.indexOf('-') + 2, o1.indexOf("miles") - 1);
                distance2 = o2.substring(o2.indexOf('-') + 2, o2.indexOf("miles") - 1);

                int x = Integer.parseInt(distance1);
                int y = Integer.parseInt(distance2);

                if (x == y) {
                    return 0;
                }
                if (x > y) {
                    return 1;
                }
                if (x < y) {
                    return -1;
                }
                return 0;
            }
        });

        sortSpaceList(1);
        adapter.notifyDataSetChanged();





    }


    public void sortSpaceList(int n)
    {
        //0 means byPrice
        if(n==0)
        {
            Collections.sort(spaceList, new Comparator<ObjectClasses.Space>()
            {
                @Override
                public int compare(ObjectClasses.Space o1, ObjectClasses.Space o2)
                {
                    int p1 = o1.getPricePerHour();
                    int p2 = o2.getPricePerHour();

                    if(p1 == p2)
                    {
                        return 0;
                    }
                    else if(p1 < p2)
                    {
                        return -1;
                    }
                    else if(p1 > p2)
                    {
                        return 1;
                    }

                    return 0;
                }
            });
        }

        if(n==1)
        {
            Collections.sort(spaceList, new Comparator<ObjectClasses.Space>()
            {
                @Override
                public int compare(ObjectClasses.Space o1, ObjectClasses.Space o2)
                {

                    String o1FullAddress = o1.getStreetAddress() + " " + o1.getCity() + " " +
                            o1.getState() + " " + o1.getZipCode();
                    String o2FullAddress = o2.getStreetAddress() + " " + o2.getCity() + " " +
                            o2.getState() + " " + o2.getZipCode();

                    //TODO: Use userlocation member variable latlng, downcast floats to ints
                    int p1 = (int)calculateDistanceFromTo(latLngFromAddress(o1FullAddress), userAddress);
                    int p2 = (int)calculateDistanceFromTo(latLngFromAddress(o2FullAddress), userAddress);

                    if(p1 == p2)
                    {
                        return 0;
                    }
                    else if(p1 < p2)
                    {
                        return -1;
                    }
                    else if(p1 > p2)
                    {
                        return 1;
                    }

                    return 0;
                }
            });
        }
    }

    public float calculateDistanceFromTo(LatLng from, LatLng to)
    {
        float distanceMiles;
        float[] results = new float[1];
        Location.distanceBetween(from.latitude, from.longitude,
                to.latitude, to.longitude,
                results);

        distanceMiles = (float) results[0]/(float)0.00062137;

        return distanceMiles;
    }
}
