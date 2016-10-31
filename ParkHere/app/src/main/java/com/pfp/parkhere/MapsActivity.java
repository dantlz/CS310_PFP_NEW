package com.pfp.parkhere;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ObjectClasses.MyCalendar;
import ObjectClasses.Space;
import ObjectClasses.SpaceType;
import ObjectClasses.Status;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ShareActionProvider mShareActionProvider;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Map<LatLng, Space> allSpaces = new HashMap<>();
    private List<LatLng> filteredSpaces = new LinkedList<>();
    private LocationManager locationManager;
    private static Status userMode;
    private static String spaceMode = "Compact";
    private LocationListener locationListener;
    private MenuItem addSpaceItem;
    private MenuItem registerAsBothItem;
    private Marker addSpaceMarker;
    private Button filtersButton;
    private Button resultAsListButton;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        filtersButton = (Button) findViewById(R.id.filtersButton);
        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SearchFiltersActivity.class);
                startActivityForResult(intent, 123);
            }
        });

        resultAsListButton = (Button) findViewById(R.id.resultListButton);
        resultAsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MapsActivity.this, .class);
        //TODO Go to RESULT LIST startActivityForResult(, 123);
            }
        });

        if(Global_ParkHere_Application.getCurrentUserObject().getStatus().equals(Status.OWNER)){
            ((RadioButton)findViewById(R.id.Owner)).setChecked(true);
            (findViewById(R.id.Seeker)).setEnabled(false);
            ownerMode(true);
        }
        else if(Global_ParkHere_Application.getCurrentUserObject().getStatus().equals(Status.SEEKER)){
            ((RadioButton)findViewById(R.id.Seeker)).setChecked(true);
            (findViewById(R.id.Owner)).setEnabled(false);
            seekerMode(true);
        }
        else{
            //Registered as both owner and seeker
            //Remember last time's preferred status
            if(Global_ParkHere_Application.getCurrentUserObject().getPreferredStatus().equals(Status.SEEKER)) {
                ((RadioButton) findViewById(R.id.Seeker)).setChecked(true);
                seekerMode(true);
            }
            else{
                ((RadioButton) findViewById(R.id.Owner)).setChecked(true);
                userMode = Status.OWNER;
                ownerMode(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase.getInstance().getReference().child("Spaces").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot peer : dataSnapshot.getChildren()) {
                            for (DataSnapshot spce : peer.getChildren()) {
                                Space space = spce.getValue(Space.class);
                                Address address = null;
                                try {
                                    address = new Geocoder(MapsActivity.this)
                                            .getFromLocationName(space.getStreetAddress() + space.getCity()
                                                    + space.getState() + " " + space.getZipCode(), 1).get(0);
                                    allSpaces.put(new LatLng(address.getLatitude(), address.getLongitude()), space);
                                    addMarkers(null);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //This is a listed space marker
                if(allSpaces.get(marker.getPosition()) != null) {
                    Space selectedSpace = allSpaces.get(marker.getPosition());
                    Intent intent = new Intent(MapsActivity.this, MyListedSpacesDetailsActivity.class);
                    intent.putExtra("SPACENAME", selectedSpace.getSpaceName());
                    intent.putExtra("OWNEREMAIL", selectedSpace.getOwnerEmail());
                    intent.putExtra("STATUS", String.valueOf(userMode));
                    startActivity(intent);
                }
                //This is an owner listing a new space
                else{
                    Intent intent = new Intent(MapsActivity.this, AddSpaceActivity.class);
                    intent.putExtra("LAT", marker.getPosition().latitude);
                    intent.putExtra("LNG", marker.getPosition().longitude);
                    startActivity(intent);
                }
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Only owners can drop new pins
                if(!userMode.equals(Status.OWNER)){
                    return;
                }

                if(addSpaceMarker != null){
                    addSpaceMarker.remove();
                }
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                marker.title("List a space here");
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                addSpaceMarker = mMap.addMarker(marker);
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(addSpaceMarker != null){
                    addSpaceMarker.remove();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void addMarkers(Intent intent) {
        mMap.clear();

        if(intent != null){
            Bundle extras = intent.getExtras();
            SpaceType type = (SpaceType) extras.get("TYPE");
            int lowestPrice = extras.getInt("LOWESTPRICE");
            int highestPrice = extras.getInt("HIGHESTPRICE");
            SimpleDateFormat format = new SimpleDateFormat();
//            "yyyy.MM.dd G 'at' HH:mm:ss z"
//            Year2016 month 9 day 30 hour 20 minute 0
//            2016.09.30.20.00
            String month = "";
            if(extras.getInt("STARTMONTH") < 10){
                month = "0" + extras.getInt("STARTMONTH");
            }
            else{
                month = "" + extras.getInt("STARTMONTH");
            }

            String day = "";
            if(extras.getInt("STARTDAY") < 10){
                day = "0" + extras.getInt("STARTDAY");
            }
            else{
                day = "" + extras.getInt("STARTDAY");
            }

            String hour = "";
            if(extras.getInt("STARTHOUR") < 10){
                hour = "0" + extras.getInt("STARTHOUR");
            }
            else{
                hour = "" + extras.getInt("STARTHOUR");
            }

            String minute = "";
            if(extras.getInt("STARTMINUTE") < 10){
                minute = "0" + extras.getInt("STARTMINUTE");
            }
            else{
                minute = "" + extras.getInt("STARTMINUTE");
            }

            String fullDateTime = extras.getInt("STARTYEAR") + "."
                    + month + "." + day + "." + hour + "." + minute;
            System.out.println(fullDateTime);
            //TODO Finish filtering here
//            Date startDateTime = format.parse()
//            MyCalendar(
//                    startDatePicker.getYear(),
//                    startDatePicker.getMonth(),
//                    startDatePicker.getDayOfMonth(),
//                    startTimePicker.getHour(),
//                    startTimePicker.getMinute()
//            ));
//            listedSpace.setAvailableEndDateAndTime(new MyCalendar(
//                    endDatePicker.getYear(),
//                    endDatePicker.getMonth(),
//                    endDatePicker.getDayOfMonth(),
//                    endTimePicker.getHour(),
//                    endTimePicker.getMinute()
//            ));
//
//            intent.putExtra("STARTYEAR", startDatePicker.getYear());
//            intent.putExtra("STARTMONTH", startDatePicker.getMonth());
//            intent.putExtra("STARTDAY", startDatePicker.getDayOfMonth());
//            intent.putExtra("STARTHOUR", startTimePicker.getHour());
//            intent.putExtra("STARTMINUTE", startTimePicker.getMinute());
//            intent.putExtra("ENDYEAR", endDatePicker.getYear());
//            intent.putExtra("ENDMONTH", endDatePicker.getMonth());
//            intent.putExtra("ENDDAY", endDatePicker.getDayOfMonth());
//            intent.putExtra("ENDHOUR", endTimePicker.getHour());
//            intent.putExtra("ENDMINUTE", endTimePicker.getMinute());
        }

        Iterator it = allSpaces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Space space = (Space) pair.getValue();
            MarkerOptions marker = new MarkerOptions();
            marker.position((LatLng)pair.getKey());
            marker.title(space.getSpaceName());
            mMap.addMarker(marker);
        }
    }

    //This allows search by BOTH address AND LONGITUDE LATITUDE
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        Geocoder geocoder;
        if (location != null || !location.equals("")) {
            geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //TODO ALWAYS keep the zoom at 3 miles and to remove any spaces over 3 miles
            //TODO Create 2 lists of spaces. One is all, attached to firebase, other is search results
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.Owner:
                ownerMode(false);
                break;
            case R.id.Seeker:
                seekerMode(false);
                break;
        }
    }

    private void ownerMode(boolean firstTime){
        userMode = Status.OWNER;
        if(!firstTime)
            addSpaceItem.setEnabled(true);
        filtersButton.setVisibility(View.GONE);
        resultAsListButton.setVisibility(View.GONE);
    }

    private void seekerMode(boolean firstTime){
        userMode = Status.SEEKER;
        if(!firstTime) {
            addSpaceItem.setEnabled(false);
            if (addSpaceMarker != null) {
                addSpaceMarker.remove();
            }
        }
        filtersButton.setVisibility(View.VISIBLE);
        resultAsListButton.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemProfile:
                // User chose the "Profile" action, change to that Activity Screen
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                intent.putExtra("Status", String.valueOf(userMode));
                startActivity(intent);
                return true;
            case R.id.itemAddSpace:
                startActivity(new Intent(MapsActivity.this, AddSpaceActivity.class));
                return true;
            case R.id.Both:
                startActivity(new Intent(MapsActivity.this, BecomeBothOwnerAndSeeker.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                System.exit(0);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }

        };

        // If device is running SDK < 23
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, locationListener);

        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                // we have permission!
                locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123){
            addMarkers(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        addSpaceItem = menu.getItem(0);
        registerAsBothItem = menu.getItem(2);
        if(Global_ParkHere_Application.getCurrentUserObject().getStatus().equals(Status.BOTH)){
            registerAsBothItem.setEnabled(false);

        }
        return super.onCreateOptionsMenu(menu);
    }

}