package com.pfp.parkhere;

import android.Manifest;
import android.app.Dialog;
import android.os.Handler;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

import ObjectClasses.MyCalendar;
import ObjectClasses.Space;
import ObjectClasses.SpaceType;
import ObjectClasses.Status;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Map<LatLng, Space> allSpaces = new HashMap<>();
    private LocationManager locationManager;
    private static Status userMode;
    private LocationListener locationListener;
    private MenuItem addSpaceItem;
    private MenuItem registerAsBothItem;
    private Marker addSpaceMarker;
    private Button filtersButton;
    private Button resultAsListButton;
    private SupportMapFragment mapFragment;
    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
    private LatLng currentCameraOrZoomLatLng;
    private Intent currentFiltersIntent;
    private Handler handler;
    private Runnable handlerRunnable;
    private boolean runnableRunning;
    private boolean onlyThreeMileRadius;
    private int counter = 0;

    //TODO Add progress bar/loading screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        handler = new Handler() {
            public void publish(LogRecord record) {}

            public void flush() {}

            public void close() throws SecurityException {}
        };

        handlerRunnable = new Runnable() {
            @Override
            public void run() {
                //On initial map load we don't have to search because Firebase initial listener does that
                if(counter == 0) {
                    counter++;
                    return;
                }
                //If the camera is zoomed in enough, display only results in 3 miles
                if(mMap.getCameraPosition().zoom > 13.505731){
                    onlyThreeMileRadius = true;
                }
                else{
                    onlyThreeMileRadius = false;
                }
                addAndFilterMarkers(currentFiltersIntent);
            }
        };
        runnableRunning = false;

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
                Intent intent = new Intent(MapsActivity.this, ResultsActivity.class);
                intent.putExtra("LATLNG", currentCameraOrZoomLatLng);
                Global.setMapOfLatLngSpacesToPass(filterForResultsList(currentFiltersIntent));
                startActivity(intent);
            }
        });


        if(Global.getCurUser().getStatus().equals(Status.OWNER)){
            ((RadioButton)findViewById(R.id.Owner)).setChecked(true);
            (findViewById(R.id.Seeker)).setEnabled(false);
            ownerMode(true);
        }
        else if(Global.getCurUser().getStatus().equals(Status.SEEKER)){
            ((RadioButton)findViewById(R.id.Seeker)).setChecked(true);
            (findViewById(R.id.Owner)).setEnabled(false);
            seekerMode(true);
        }
        else{
            //Registered as both owner and seeker
            //Remember last time's preferred status
            if(Global.getCurUser().getPreferredStatus().equals(Status.SEEKER)) {
                ((RadioButton) findViewById(R.id.Seeker)).setChecked(true);
                seekerMode(true);
            }
            else{
                ((RadioButton) findViewById(R.id.Owner)).setChecked(true);
                userMode = Status.OWNER;
                ownerMode(true);
            }
        }

        //Must verify
        if(Global.getCurUser().getPhotoID() == null ||
                Global.getCurUser().getPhotoID().equals("")){
            Dialog dialog = new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Verification Needed")
                    .setMessage("You must verify your identity by uploading your photo ID to use ParkHere")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MapsActivity.this, VerificationActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        if(Global.getCurUser().getPhotoID() == null ||
                Global.getCurUser().getPhotoID().equals("")){
            Dialog dialog = new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Verification Needed")
                    .setMessage("You must verify your identity by uploading your photo ID to use ParkHere")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MapsActivity.this, VerificationActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        super.onResume();
    }

    private HashMap<LatLng, Space> filterForResultsList(Intent intent) {
        HashMap<LatLng, Space> result = new HashMap<>();
        int lowestPrice = 0, highestPrice = 0;
        SpaceType type = null;
        Date startDateTime = null, endDateTime = null;

        if (intent != null) {
            Bundle extras = intent.getExtras();
            type = (SpaceType) extras.get("TYPE");
            lowestPrice = extras.getInt("LOWESTPRICE");
            highestPrice = extras.getInt("HIGHESTPRICE");
            startDateTime = extraToDate(extras, "START");
            endDateTime = extraToDate(extras, "END");
        }

        Iterator it = allSpaces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Space space = (Space) pair.getValue();
            //Filtering
            if (intent != null) {
                if (!space.getType().equals(type))
                    continue;
                if (space.getPricePerHour() < lowestPrice || space.getPricePerHour() > highestPrice)
                    continue;
                MyCalendar start = space.getAvailableStartDateAndTime();
                MyCalendar end = space.getAvailableEndDateAndTime();
                if (myCalendarToDate(start).before(startDateTime) || myCalendarToDate(end).before(endDateTime))
                    continue;
            }

            if (onlyThreeMileRadius) {
                if (!checkThreeMileRadius(currentCameraOrZoomLatLng, (LatLng) pair.getKey()))
                    continue;
            }

            result.put((LatLng) pair.getKey(), (Space) pair.getValue());
        }
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentCameraOrZoomLatLng = mMap.getCameraPosition().target;

        //All markers are refreshed each time something about spaces is changed on Firebase. This may cause lag
        Global.spaces().addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot peer : dataSnapshot.getChildren()) {
                            for (DataSnapshot spce : peer.getChildren()) {
                                Space space = spce.getValue(Space.class);
                                try {
                                    List<Address> addressList = new Geocoder(MapsActivity.this)
                                            .getFromLocationName(space.getStreetAddress() + space.getCity()
                                                    + space.getState() + " " + space.getZipCode(), 1);
                                    //If the space's LatLng could not be located, just skip it to avoid crashes or lags
                                    if(addressList.size() < 1)
                                        continue;
                                    allSpaces.put(new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude()), space);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        addAndFilterMarkers(null);
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
                    intent.putExtra("SPACE_NAME", selectedSpace.getSpaceName());
                    intent.putExtra("SPACE_OWNEREMAIL", selectedSpace.getOwnerEmail());
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
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                currentCameraOrZoomLatLng = mMap.getCameraPosition().target;

                //This could potentially have the map refreshed too often, causing lags and crashes
                if(handler != null && !runnableRunning) {
                    handler.postDelayed(handlerRunnable, 1300);
                    runnableRunning = true;
                }
            }
        });
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                handler.removeCallbacks(handlerRunnable);
                runnableRunning = false;
            }
        });

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(34, -118) , 4.0f) );

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //This allows search by BOTH address AND LONGITUDE LATITUDE
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        //No text entered to search bar
        if(locationSearch.getText().toString().equals("")){
            return;
        }
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void setCurrentSearchTimeFrame(Intent intent){
        Bundle extras = intent.getExtras();

        MyCalendar start = new MyCalendar();
        start.setYear(extras.getInt("STARTYEAR"));
        start.setMonth(extras.getInt("STARTMONTH"));
        start.setDay(extras.getInt("STARTDAY"));
        start.setHour(extras.getInt("STARTHOUR"));
        start.setMinute(extras.getInt("STARTMINUTE"));
        Global.setCurrentSearchTimeDateStart(start);

        MyCalendar end = new MyCalendar();
        end.setYear(extras.getInt("ENDYEAR"));
        end.setMonth(extras.getInt("ENDMONTH"));
        end.setDay(extras.getInt("ENDDAY"));
        end.setHour(extras.getInt("ENDHOUR"));
        end.setMinute(extras.getInt("ENDMINUTE"));
        Global.setCurrentSearchTimedateEnd(start);
    }

    private String getDoubleDigit(int i){
        String result = "";
        if(i < 10){
            result = "0" + i;
        }
        else{
            result = "" + i;
        }

        return result;
    }

    private Date extraToDate(Bundle extras, String SoE){
        String year, month, day, hour, minute;
        Date dateTime;

        year = String.valueOf(extras.getInt(SoE + "YEAR")).substring(2);
        month = getDoubleDigit(extras.getInt(SoE + "MONTH"));
        day = getDoubleDigit(extras.getInt(SoE + "DAY"));
        hour = getDoubleDigit(extras.getInt(SoE + "HOUR"));
        minute = getDoubleDigit(extras.getInt(SoE + "MINUTE"));
        String fullStartDateTime = year + month + day + hour + minute + "00-0700";
        System.out.println(fullStartDateTime);
        try {
            dateTime = format.parse(fullStartDateTime);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date myCalendarToDate(MyCalendar calendar){
        String year, month, day, hour, minute;
        Date dateTime;

        year = String.valueOf(calendar.getYear()).substring(2);
        month = getDoubleDigit(calendar.getMonth());
        day = getDoubleDigit(calendar.getDay());
        hour = getDoubleDigit(calendar.getHour());
        minute = getDoubleDigit(calendar.getMinute());
        String sdt = year + month + day + hour + minute + "00-0700";
        try {
            dateTime = format.parse(sdt);
            return  dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addAndFilterMarkers(Intent intent) {
        System.out.println("Adding and filtering markers");
        mMap.clear();

        int lowestPrice = 0, highestPrice = 0;
        SpaceType type = null;
        Date startDateTime = null, endDateTime = null;

        if(intent != null){
            Bundle extras = intent.getExtras();
            type = (SpaceType) extras.get("TYPE");
            lowestPrice = extras.getInt("LOWESTPRICE");
            highestPrice = extras.getInt("HIGHESTPRICE");
            startDateTime = extraToDate(extras, "START");
            endDateTime = extraToDate(extras, "END");
        }

        Iterator it = allSpaces.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Space space = (Space) pair.getValue();
            //Filtering
            if(intent != null){
                if(!space.getType().equals(type))
                    continue;
                if(space.getPricePerHour() < lowestPrice || space.getPricePerHour() > highestPrice)
                    continue;
                MyCalendar start = space.getAvailableStartDateAndTime();
                MyCalendar end = space.getAvailableEndDateAndTime();
                if(myCalendarToDate(start).before(startDateTime) || myCalendarToDate(end).before(endDateTime))
                    continue;
            }

            if(onlyThreeMileRadius) {
                if (!checkThreeMileRadius(currentCameraOrZoomLatLng, (LatLng) pair.getKey()))
                    continue;
            }

            MarkerOptions marker = new MarkerOptions();
            marker.position((LatLng)pair.getKey());
            marker.title(space.getSpaceName());
            mMap.addMarker(marker);
        }

        if(onlyThreeMileRadius)
            Toast.makeText(getApplicationContext(), "Search Radius: 3 Miles", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Search Radius: Unlimited", Toast.LENGTH_SHORT).show();

    }

    public boolean checkThreeMileRadius(LatLng here, LatLng dist) {
        boolean withinRadius;
        float[] results = new float[1];
        Location.distanceBetween(here.latitude, here.longitude,
                dist.latitude, dist.longitude,
                results);

        if (results[0] <= 4828.03) {
            withinRadius = true;
        }
        else {
            withinRadius = false;
        }
        return withinRadius;
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
        Global.curUserRef().child("preferredStatus").setValue(Status.OWNER);
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
        Global.curUserRef().child("preferredStatus").setValue(Status.SEEKER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.itemProfile:
                // User chose the "Profile" action, change to that Activity Screen
                intent = new Intent(MapsActivity.this, ProfileActivity.class);
                intent.putExtra("USER_STATUS", String.valueOf(userMode));
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
                intent = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Result from selecting filters
        if(requestCode == 123){
            if(resultCode == 12321) {
                setCurrentSearchTimeFrame(data);
                currentFiltersIntent = data;
                addAndFilterMarkers(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        addSpaceItem = menu.getItem(0);
        registerAsBothItem = menu.getItem(2);
        if(Global.getCurUser().getStatus().equals(Status.BOTH)){
            registerAsBothItem.setEnabled(false);

        }
        if(Global.getCurUser().getStatus().equals(Status.SEEKER)){
            addSpaceItem.setEnabled(false);
        }
        if(userMode.equals(Status.SEEKER))
            addSpaceItem.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(addSpaceMarker != null)
            addSpaceMarker.remove();
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
}