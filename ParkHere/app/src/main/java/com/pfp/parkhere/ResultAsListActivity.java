package com.pfp.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ObjectClasses.Space;
//Done Sprint 2
public class ResultAsListActivity extends Activity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private Map<LatLng, ObjectClasses.Space> spacesLatLng;
    private LatLng userLatLng;

    private ArrayList<String> viewValues;
    private ArrayList<ObjectClasses.Space> spaceList;
//TODO Very strange issue. If new install of app or autologin, then populates with no probelm. If sign out then sign in, it crashes because listview didn't have any children. A rough fix is put into place
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        userLatLng = (LatLng)getIntent().getExtras().get("LATLNG");
        spacesLatLng = Global.getMapOfLatLngSpacesToPass();
        spaceList = new ArrayList<>();
        viewValues = new ArrayList<>();

        listView = (ListView) findViewById(R.id.resultsListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int n = position;
                ObjectClasses.Space spx = spaceList.get(n);
                Intent intent = new Intent(ResultAsListActivity.this, MySpaceDetails.class);
                intent.putExtra("SPACE_NAME", spx.getSpaceName());
                intent.putExtra("SPACE_OWNEREMAIL", spx.getOwnerEmail());
                //Stack depth 2
                startActivity(intent);
                finish();
            }
        });

        for (Map.Entry<LatLng, ObjectClasses.Space> spaceEntry : spacesLatLng.entrySet()) {
            LatLng latlng = spaceEntry.getKey(); //LatLng Object
            Space space = spaceEntry.getValue(); //Space Object
            viewValues.add(space.getOwnerEmail() + " $" + space.getPricePerHour() + " - " + (int)calculateDistanceFromTo(latlng, userLatLng) + " miles");
            spaceList.add(space);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewValues);
        listView.setAdapter(adapter);
        createShading();
    }

    private void createShading(){
        Global.spaceReviews().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < spaceList.size(); i ++){
                    Space space = spaceList.get(i);
                    String ownerEmail = space.getOwnerEmail();
                    String spaceName = space.getSpaceName();

                    int reviewCount = (int) dataSnapshot.child(Global.reformatEmail(ownerEmail)).child(spaceName).getChildrenCount();

                    System.out.println("Spacelatlng: " + spacesLatLng.size());
                    System.out.println("viewValues: " + viewValues.size());
                    System.out.println("Listview count: " + listView.getCount() + " Listview childrenCount" + listView.getChildCount());
                    System.out.println("Index: " + i);

                    boolean hasChildren = false;
                    if(listView.getChildCount() != 0)
                        hasChildren = true;

                    if(reviewCount > 5){
                        if(hasChildren)
                            listView.getChildAt(i).setBackgroundColor(Color.parseColor("#4682B4"));
                        else
                            getViewByPosition(i, listView).setBackgroundColor(Color.parseColor("#4682B4"));
                    }
                    else if(reviewCount == 0){
                        if(hasChildren)
                            listView.getChildAt(i).setBackgroundColor(Color.parseColor("#87CEFA"));
                        else
                            getViewByPosition(i, listView).setBackgroundColor(Color.parseColor("#87CEFA"));
                    }
                    else{
                        if(hasChildren)
                            listView.getChildAt(i).setBackgroundColor(Color.parseColor("#00BFFF"));
                        else
                            getViewByPosition(i, listView).setBackgroundColor(Color.parseColor("#00BFFF"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } /*else {
        final int childIndex = pos - firstListItemPosition;
        return listView.getChildAt(childIndex);
    }*/ // Try with else part un-commented too
        return null;
    }

    public void sortByPrice(View view) {
        Collections.sort(viewValues, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer dollapos1 = o1.indexOf('$');
                Integer dollapos2 = o2.indexOf('$');
                String s1p = "";
                String s2p = "";
                for(int i=dollapos1+1; i<o1.indexOf('-')-1; i++) {
                    s1p+=o1.charAt(i);
                }

                for(int i=dollapos2+1; i<o2.indexOf('-')-1; i++) {
                    s2p+=o2.charAt(i);
                }

                Integer x = Integer.parseInt(s1p);
                Integer y = Integer.parseInt(s2p);

                if(x==y)
                    return 0;
                else if(x<y)
                    return -1;
                else if(x>y)
                    return 1;

                return 0;
            }
        });

        sortSpaceList(0);
        adapter.notifyDataSetChanged();
        createShading();
    }

    public void sortByDistance(View view) {
        Collections.sort(viewValues, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                String distance1 = "";
                String distance2 = "";
                distance1 = o1.substring(o1.indexOf('-') + 2, o1.indexOf("miles") - 1);
                distance2 = o2.substring(o2.indexOf('-') + 2, o2.indexOf("miles") - 1);

                int x = Integer.parseInt(distance1);
                int y = Integer.parseInt(distance2);

                if (x == y)
                    return 0;
                if (x > y)
                    return 1;
                if (x < y)
                    return -1;

                return 0;
            }
        });

        sortSpaceList(1);
        adapter.notifyDataSetChanged();
        createShading();
    }

    private void sortSpaceList(final int n) {
        //0 means byPrice
        if(n==0) {
            Collections.sort(spaceList, new Comparator<ObjectClasses.Space>() {
                @Override
                public int compare(ObjectClasses.Space o1, ObjectClasses.Space o2) {
                    int p1 = o1.getPricePerHour();
                    int p2 = o2.getPricePerHour();

                    if(p1 == p2)
                        return 0;
                    else if(p1 < p2)
                        return -1;
                    else if(p1 > p2)
                        return 1;

                    return 0;
                }
            });
        }

        if(n==1) {
            Collections.sort(spaceList, new Comparator<ObjectClasses.Space>() {
                @Override
                public int compare(ObjectClasses.Space o1, ObjectClasses.Space o2) {
                    LatLng o1LatLng = null, o2LatLng = null;
                    for (Map.Entry<LatLng, Space> e : spacesLatLng.entrySet()) {
                        if(e.getValue().equals(o1))
                            o1LatLng = e.getKey();
                        if(e.getValue().equals(o2))
                            o2LatLng = e.getKey();
                    }

                    int p1 = (int)calculateDistanceFromTo(o1LatLng, userLatLng);
                    int p2 = (int)calculateDistanceFromTo(o2LatLng, userLatLng);

                    if(p1 == p2)
                        return 0;
                    else if(p1 < p2)
                        return -1;
                    else if(p1 > p2)
                        return 1;

                    return 0;
                }
            });
        }
    }

    private float calculateDistanceFromTo(LatLng from, LatLng to) {
        float distanceMiles;
        float[] results = new float[1];
        Location.distanceBetween(from.latitude, from.longitude,
                to.latitude, to.longitude,
                results);

        distanceMiles = results[0]/(float)0.00062137;

        return distanceMiles;
    }

}
