package com.f2w.arpit.college_map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,  NavigationView.OnNavigationItemSelectedListener {
    String[] locations = {"Main Gate", "Administrative Block", "Learning Resource Center (LRC)", "Convention Center", "Cafeteria Canteen", "Main Pandaal", "Block-II", "LT-1", "Block-III", "Block-IV", "Block-VI", "LT-2", "Block-V", "Block-I", "Hospital", "Open Air Theatre (OAT)", "Sports Complex", "MDP", "Visitors Hostel"};
    String[][][] p = {
            {	{"0",""},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"1" , ""},	{"18",""},	{"18",""},	{"17","q}e_Dwxc|MNr@JAHFDJBNnCq@?ODWnAqC"},	{"18","q}e_Dwxc|MLr@R@HJBRnCq@HAN@PFbFxCD@DADCnGdD"}	},
            {	{"0",""},	{"1",""},	{"2","_ze_Dkkc|MfAhH"},	{"3","_ze_Dkkc|Mb@xCfAWDAD@D@B?~@Y"},	{"2",""},	{"2",""},	{"7",""},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"14","_ze_Dkkc|M@FcBZQ@ODIJCNEPe@JBV"},	{"18",""},	{"18",""},	{"17","_ze_Dkkc|M@SMMeA{GJIBMnCq@?ODWnAqC"},	{"18","_ze_Dkkc|M@SMMeA{GJIBMnCq@N@PFbFxCD@DADCnGdD"}	},
            {	{"0",""},	{"0",""},	{"2",""},	{"3","}we_Dcbc|M]mCfAWDAD@D@B?~@Y"},	{"4","wwe_Dabc|My@HB`@]H"},	{"5","wwe_Dabc|My@H@`@H`@x@hB"},	{"7",""},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"1",""},	{"1",""},	{"1",""},	{"1",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"3",""},	{"2","}we_Dcbc|M]mCfAWDAD@D@B?~@Y"},	{"5","}se_Dahc|M?F?D@BBBBBD?BAHZTEPFiCpGGb@MB"},	{"7",""},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"1",""},	{"1",""},	{"1",""},	{"1",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"4",""},	{"5","ize_Dm`c|MLCtAbC"},	{"7",""},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"2",""},	{"2",""},	{"2",""},	{"2",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"5",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"4",""},	{"2",""},	{"2",""},	{"4",""},	{"4",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"6",""},	{"7","y}e_Dcdc|M_@SEO"},	{"7",""},	{"13",""},	{"13",""},	{"13",""},	{"13",""},	{"13","uaf_Dmdc|MtAY"},	{"14","__f_Deec|MCQGBKy@"},	{"14",""},	{"14",""},	{"14",""},	{"14",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"7",""},	{"8","y}e_Dcdc|MS\\@P"},	{"8",""},	{"8",""},	{"8",""},	{"6",""},	{"6",""},	{"6",""},	{"1",""},	{"1",""},	{"1",""},	{"1",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"8",""},	{"9","i~e_Dubc|MwAZ"},	{"9",""},	{"9",""},	{"9",""},	{"9",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""},	{"7",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"9",""},	{"10","ecf_Deac|MbAS"},	{"10",""},	{"13",""},	{"13","aaf_Dyac|MSqA"},	{"13",""},	{"8",""},	{"8",""},	{"13",""},	{"13",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"10",""},	{"11","ecf_Deac|MWFCQYQ"},	{"11",""},	{"9",""},	{"9",""},	{"9",""},	{"9",""},	{"9",""},	{"9",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"11",""},	{"12","{cf_Dwcc|MSD@NK\\"},	{"12",""},	{"12",""},	{"10",""},	{"10",""},	{"12",""},	{"12",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"12",""},	{"13","uaf_Dmdc|MgAT"},	{"13",""},	{"13",""},	{"13",""},	{"13",""},	{"13",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"13",""},	{"6",""},	{"6",""},	{"6",""},	{"6",""},	{"6",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"14",""},	{"1",""},	{"1",""},	{"1",""},	{"1",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"15",""},	{"16","oge_Dibc|MUnAd@JDD@J@L"},	{"16",""},	{"16",""}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"16",""},	{"18",""},	{"18","_fe_Dmmc|Mj@XBF?H]dA_ArF"}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"17",""},	{"18","{te_Do}c|MkAnCEV?PR@TF`FxCFA|GfD"}	},
            {	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"18",""}	}
    };
    String[] publicToilets = {"pt0","pt1","pt2","pt3","pt4"};
    String origin=null, destination=null;

//    Map<String, LatLng> ptl = new HashMap<String, LatLng>();
//    LatLng a=new LatLng(26.24655, 78.17255));
//    ptl.Put("a",a);

    private GoogleMap mMap, map;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    private LocationRequest mLocationRequest;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    final static int LOCATION_SETTINGS_REQUEST = 199;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private long UPDATE_INTERVAL = 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 500; /* 2 sec */
//    kmjnvn, ijhn
    boolean hospital_flag=false,mobile_toilet_flag=false,ambulance_flag=false, flag1 = false, flag_for_start = false, zoom_flag = true, marker_flag=false, event_flag=false;
    List<Marker> all_mMarkers = new ArrayList<Marker>();
    List<Marker> all_mMarkers_toilet = new ArrayList<Marker>();
    List<LatLng> all_points = new ArrayList<LatLng>();
    FloatingActionButton start, stop, go;
    int flag2 = 2;
    SupportMapFragment mapFragment;
    MenuItem label;
    Button orig, des;
    Marker marker1;
    FusedLocationProviderClient m;
    private ArrayList<SearchModel> initData(){
        ArrayList<SearchModel> items = new ArrayList<>();
        items.add(new SearchModel("Main Gate"));
        items.add(new SearchModel("Administrative Block"));
        items.add(new SearchModel("Learning Resource Center (LRC)"));
        items.add(new SearchModel("Convention Center"));
        items.add(new SearchModel("Cafeteria Canteen"));
        items.add(new SearchModel("Main Pandaal"));
        items.add(new SearchModel("Block-II"));
        items.add(new SearchModel("LT-1"));
        items.add(new SearchModel("Block-III"));
        items.add(new SearchModel("Block-IV"));
        items.add(new SearchModel("Block-VI"));
        items.add(new SearchModel("LT-2"));
        items.add(new SearchModel("Block-V"));
        items.add(new SearchModel("Block-I"));
        items.add(new SearchModel("Hospital"));
        items.add(new SearchModel("Open Air Theatre (OAT)"));
        items.add(new SearchModel("Sports Complex"));
        items.add(new SearchModel("MDP"));
        items.add(new SearchModel("Visitors Hostel"));
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
//        pd = new ProgressDialog(this);
//        pd.setMessage("Starting Navigation");
        //My
        final String[] destination1 = new String[1];
        final String[] source = new String[1];
        orig = (Button) findViewById(R.id.origin);
        des = (Button) findViewById(R.id.dest);
        if(orig.getText().toString()!="    Your Current Location")
            origin=orig.getText().toString();
        if(des.getText().toString()!="    Your Destiation Location")
            destination=orig.getText().toString();

        findViewById(R.id.origin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(MapsActivity.this, "Search...", "Where are you now ?",
                        null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(MapsActivity.this, "" + searchable.getTitle(), Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();
                        source[0] = searchable.getTitle();
                        orig.setText(source[0]);
                    }
                }).show();
            }
        });

        findViewById(R.id.dest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(MapsActivity.this, "Search...", "Where are you now ?",
                        null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(MapsActivity.this, "" + searchable.getTitle(), Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();
                        destination1[0] = searchable.getTitle();
                        des.setText(destination1[0]);
                    }
                }).show();
            }
        });
        destination = getIntent().getStringExtra("Destination");
        if(destination!=null){
            des.setText(destination);
        }

        go = (FloatingActionButton) findViewById(R.id.go);
        start = (FloatingActionButton) findViewById(R.id.start);
        stop = (FloatingActionButton)findViewById(R.id.stop);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                intent.putExtra("Destination", des.getText().toString());
                intent.putExtra("Origin", orig.getText().toString());
                String s=orig.getText().toString();
                if(orig.getText().toString() == ""|| des.getText().toString() == "")
                    Toast.makeText(MapsActivity.this, "Please give both inputs", Toast.LENGTH_SHORT).show();
                else if(orig.getText().toString().equals(des.getText().toString()))
                    Toast.makeText(MapsActivity.this, "Please give different inputs", Toast.LENGTH_SHORT).show();
                if (orig.getText().toString()!=""&& des.getText().toString() != "")
                    intent.putExtra("flag_for_start", "true");
                if(orig.getText().toString() != "" && des.getText().toString()!= ""&&!(orig.getText().toString().equals(des.getText().toString()))){
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        startLocationUpdates();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        tvDistanceDuration = (TextView) findViewById(R.id.text);
        origin = getIntent().getStringExtra("Origin");
        destination = getIntent().getStringExtra("Destination");
        if(getIntent().getStringExtra("EventPressed")!=null) {
            event_flag = true;
            Toast.makeText(MapsActivity.this, "Please select your current location", Toast.LENGTH_SHORT).show();
        }
        String a=getIntent().getStringExtra("flag_for_start");
        if(getIntent().getStringExtra("flag_for_start")!=null)
            flag_for_start= true;
        if(getIntent().getStringExtra("hospital_flag")!=null)
            hospital_flag= true;
        if(origin!=null||destination!=null){
            orig.setText(origin);
            des.setText(destination);

        }

        if(flag_for_start)
            start.show();
        else
            start.hide();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag1 = true;
                flag2=2;
                stop.show();
                start.hide();
//                pd.show();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag2 = 1;
                stop.hide();
                start.show();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeToLatLng(destination).latitude, placeToLatLng(destination).longitude),18));

            }
        });

        if(mMap!=null)
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        label = menu.findItem(R.id.lables);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }

int backpress=0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //To Do
            if(flag_for_start||hospital_flag){
                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                backpress = (backpress + 1);
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

                if (backpress>1) {
                    this.finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation_drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      if (id == R.id.satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (id == R.id.terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }

        return super.onOptionsItemSelected(item);
    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        return brng;
    }

void showLablesToilets(){
    CameraPosition newCamPos = new CameraPosition.Builder()
            .target(placeToLatLng(publicToilets[0]))      // Sets the center of the map to Mountain View
            .zoom(18)                    // Sets the tilt of the camera to 30 degrees
            .build();
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos));
    for(int i=0;i<3;i++){

        mobile_toilet_flag=true;
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setBackground(getDrawable(R.color.icon));
        iconFactory.setTextAppearance(R.style.iconGenText);
        Marker marker;
            String mDrawableName = "toilet1";
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            LatLng block = placeToLatLng(publicToilets[i]);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(block)
                    .title("Public Toilet")

                    .icon(BitmapDescriptorFactory.fromResource(resID)));


//        MarkerOptions markerOptions = new MarkerOptions()
//                .icon(getDrawable())
//                .position(placeToLatLng(publicToilets[i]))
//                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
//        marker = mMap.addMarker(markerOptions);
        all_mMarkers_toilet.add(marker);
    }
}
    public void hide_marker_toilets(){
//        marker_flag=false;
        mobile_toilet_flag=false;
        for (Marker marker: all_mMarkers_toilet) {
            marker.remove();
        }
    }

    Marker start_marker;
    int j=1;
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        m=getFusedLocationProviderClient(this);

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(1);

//         Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        settingsBuilder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());


        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                    m.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
//                                    if(pd!=null)
//                                        pd.dismiss();
                                    // do work here
                                    onLocationChanged(locationResult.getLastLocation());
                                }
                            },
                            Looper.myLooper());


                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(MapsActivity.this,
                                                LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS", "User denied to access location");
                    break;
            }
        }
    }
    public void onLocationChanged(Location location) {if(flag1 &&flag2==2) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18.0f);
//        double  init_distance= SphericalUtil.computeDistanceBetween(latLng, all_points.get(0));
        TreeMap<Double,LatLng> map_latlng=new TreeMap<>();
        for(int i=0;i<all_points.size();i++){

            double distance = SphericalUtil.computeDistanceBetween(latLng, all_points.get(i));
            map_latlng.put(distance,all_points.get(i));
        }

//        if(j==1)
//            start_marker=mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
//                    .title("Current Location")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
        j++;
        LatLng ahead_point=null,behind_point=null;

        for(int i=0;i<map_latlng.size();i++){
            if(SphericalUtil.computeDistanceBetween( (LatLng) map_latlng.values().toArray()[i],placeToLatLng(destination))>SphericalUtil.computeDistanceBetween(latLng,placeToLatLng(destination))){
                if(ahead_point==null)
                ahead_point =(LatLng) map_latlng.values().toArray()[i];
            }
            if(SphericalUtil.computeDistanceBetween( (LatLng) map_latlng.values().toArray()[i],placeToLatLng(destination))<SphericalUtil.computeDistanceBetween(latLng,placeToLatLng(destination))){
                if(behind_point==null)
                behind_point =(LatLng) map_latlng.values().toArray()[i];
            }
            if(ahead_point!=null&&behind_point!=null)
                break;
        }

        if(ahead_point==null)
        {
            ahead_point=latLng;
        }
        if(behind_point==null)
        {
           behind_point=latLng;
        }

        float bearing = (float) bearingBetweenLocations( ahead_point,behind_point);

//        animateMarker(start_marker,latLng,false);
//        rotateMarker(start_marker,bearing);
        LatLng mlastlocation=latLng;
        CameraPosition newCamPos = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(19)                   // Sets the zoom
                .bearing(bearing)                // Sets the orientation of the camera to east
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos));

//        mMap.moveCamera(center);
//        mMap.animateCamera(zoom);
    }

    else { }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MapsActivity.this, Photo.class);
            startActivity(intent);
            // Handle the camera action
        }else if (id == R.id.events ){
            Intent intent = new Intent(MapsActivity.this, EventList.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_gallery) {
            if(!mobile_toilet_flag)
           showLablesToilets();
            else
                hide_marker_toilets();

        } else if (id == R.id.nav_slideshow) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (id == R.id.nav_manage) {
           //hospital
            Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
            intent.putExtra("hospital_flag", "right");
            startActivity(intent);
            finish();

        }else if(id==R.id.lables)
        {   boolean f=true;
            if(marker_flag==false&&f) {

            item.setTitle("Hide markers");
            showLables();
            f=false;

        }
        if(marker_flag==true&&f) {
            f=false;
            item.setTitle("Show Markers");
            hide_marker();
        }
        }
        else if(id==R.id.map){
            Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.helpline) {

            Intent i = new Intent(MapsActivity.this, ContactUs.class);
            startActivity(i);

        }
        else if (id == R.id.arp) {

        } else if (id == R.id.pra){

        }
         else if (id == R.id.ari){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<LatLng> decodePoly1(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            all_points.add(p);
            poly.add(p);
        }
        return poly;
    }
    private  LatLng  placeToLatLng(String place){
        if(place.equals(locations[0]))//mg
        {
            return new LatLng(26.25008, 78.17640); }
        if(place.equals(locations[1]))//ad
        {
            return new LatLng(26.24942, 78.17403); }
        if(place.equals(locations[2]))//lrc
        {
            return new LatLng(26.24909, 78.17265); }
        if(place.equals(locations[3]))//na
        {
            return new LatLng(26.24846, 78.17360); }
        if(place.equals(locations[4]))//cafe
        {
            return new LatLng(26.24950, 78.17238); }
        if(place.equals(locations[5]))//cr
        {
            return new LatLng(26.24888, 78.17161); }
        if(place.equals(locations[6]))//b2
        {
            return new LatLng(26.25033608687278, 78.17326168608406); }
        if(place.equals(locations[7]))//lt1
        {
            return new LatLng(26.25002, 78.17296); }
        if(place.equals(locations[8]))//b3
        {
            return new LatLng(26.25014, 78.17262); }
        if(place.equals(locations[9]))//b4
        {
            return new LatLng(26.25053, 78.17254); }
        if(place.equals(locations[10]))//b5
        {
            return new LatLng(26.25090, 78.17247); }
        if(place.equals(locations[11]))//Lt2
        {
            return new LatLng(26.25118, 78.17268); }
        if(place.equals(locations[12]))//b6
        {
            return new LatLng(26.25105, 78.17300); }
        if(place.equals(locations[13]))//b1
        {
            return new LatLng(26.25066, 78.17311); }
        if(place.equals(locations[14]))//hosp
        {
            return new LatLng(26.25036, 78.17352); }
        if(place.equals(locations[15]))//oat
        {
            return new LatLng(26.24636, 78.17207); }
        if(place.equals(locations[16]))//sports
        {
            return new LatLng(26.24655, 78.17255); }
        if(place.equals(locations[17]))//mdp
        {
            return new LatLng(26.24888, 78.17706); }
        if(place.equals(locations[18]))//vh
        {
            return new LatLng(26.24632, 78.17410); }
        if(place.equals(publicToilets[0])) {
            return new LatLng(26.249335, 78.171364); }
        if(place.equals(publicToilets[1])) {
            return new LatLng(26.249278, 78.171870); }
        if(place.equals(publicToilets[2])) {
            return new LatLng(26.249007, 78.172228); }
        return null;
    }

    private void pathDrawn(List<LatLng> poly){
        ArrayList<LatLng> points = null;
        PolylineOptions p= null;
        points = new ArrayList<LatLng>();
        for (int l = 0; l <poly.size(); l++) {
            points.add(poly.get(l));
            p = new PolylineOptions();
            p.addAll(points);
            p.width(14);
            p.color(Color.parseColor("#59b6e8"));
        }
        mMap.addPolyline(p);
    }

    private void getDirectionAS(int o, int d) {
        while(o != d){
            int nh = Integer.parseInt(p[o][d][0]);
            if ( nh != d){
                if (o<nh){
                List<LatLng> poly = decodePoly1(p[o][nh][1]);

                pathDrawn(poly);}
                else{
                    List<LatLng> poly = decodePoly1(p[nh][o][1]);
                    pathDrawn(poly);
                }
                o = nh;
                if(o>d){
                    int t = o;
                    o=d;
                    d=t;
                }
            }
            else{
                List<LatLng> poly = decodePoly1(p[o][d][1]);
                pathDrawn(poly);
                o=d;
            }
        }
    }
    void showLables(){
//        final List<Marker> blocks_list=new ArrayList<>();
//        char a='a';
//        marker_flag=true;
//        for (int i = 6; i <= 13; i++) {
//            Marker marker;
//            String mDrawableName = "letter_" + a;
//            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
//            LatLng block = placeToLatLng(locations[i]);
//
//            marker = mMap.addMarker(new MarkerOptions()
//                    .position(block)
//                    .title(locations[i])
//
//                    .icon(BitmapDescriptorFactory.fromResource(resID)));
//            all_mMarkers.add(marker);
//            blocks_list.add(marker);
//            a++;
//
//        }

        marker_flag=true;
        for(int i=0;i<19;i++) {
            if (i != 14) {
                IconGenerator iconFactory = new IconGenerator(this);
                iconFactory.setBackground(getDrawable(R.color.icon));
                iconFactory.setTextAppearance(R.style.iconGenText);
                Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(locations[i])))
                        .position(placeToLatLng(locations[i]))
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                marker = mMap.addMarker(markerOptions);
                all_mMarkers.add(marker);
            }
        }
//        for(int i=14;i<17;i++){
//            IconGenerator iconFactory = new IconGenerator(this);
//            iconFactory.setBackground(getDrawable(R.color.icon));
//            iconFactory.setTextAppearance(R.style.iconGenText);
//            Marker marker;
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(locations[i])))
//                    .position(placeToLatLng(locations[i]))
//                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV())
//                    ;
//            marker=mMap.addMarker(markerOptions);
//            all_mMarkers.add(marker);
//
//        }
    }
    public void hide_marker(){
        marker_flag=false;
        for (Marker marker: all_mMarkers) {
            marker.remove();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocationIfPermitted();
        markerPoints = new ArrayList<LatLng>();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(16);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(26.249994, 78.176121);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //get latlong for corners for specified place
//        LatLng one = new LatLng(26.253943, 78.169293);
//        LatLng two = new LatLng(26.246070, 78.174269);
//
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        //add them to builder
//        builder.include(one);
//        builder.include(two);
//
//        LatLngBounds bounds = builder.build();

        //get width and height to current display screen
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//
//        // 20% padding
//        int padding = (int) (width * 0.10);
//
//        //set latlong bounds
//        mMap.setLatLngBoundsForCameraTarget(bounds);
        if(origin==null&&destination==null&&hospital_flag==false&&ambulance_flag==false&&mobile_toilet_flag==false||event_flag==true) {
          showLables();
        }
        if(origin.equals(""))
            origin=null;
        if(destination.equals(""))
            destination=null;
        if(origin!=null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeToLatLng(origin).latitude, placeToLatLng(origin).longitude),17));
        else  if(hospital_flag==false&&ambulance_flag==false&&mobile_toilet_flag==false)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeToLatLng("LT-1").latitude, placeToLatLng("LT-1").longitude),17));
        if(hospital_flag==true){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeToLatLng("Hospital").latitude, placeToLatLng("Hospital").longitude),17));
            IconGenerator iconFactory = new IconGenerator(this);
            iconFactory.setBackground(getDrawable(R.color.hospital));
            String mDrawableName = "letter_h";
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            LatLng block = placeToLatLng("Hospital");

            mMap.addMarker(new MarkerOptions()
                    .position(block)
                    .title("Hospital")
                    .icon(BitmapDescriptorFactory.fromResource(resID)));
       }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if(mMap.getCameraPosition().zoom<17){
                    for(Marker m:all_mMarkers) {
                        m.remove();
                    }
                }
                if(mMap.getCameraPosition().zoom>17&&marker_flag){
                   showLables();
                }
            }
        });
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        //move camera to fill the bound to screen
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
//
        int src = -1, dst = -1;
        int n = locations.length;

        if(origin!=null&&destination!=null&&(!origin.equals(destination)) ){
            for (int i = 0; i < n; i++) {
                if (locations[i].equals(origin) || locations[i].equals(destination)) {
                    src = i;
                    break;
                }
            }
            for (int i = src + 1; i < n; i++) {
                if (locations[i].equals(origin) || locations[i].equals(destination)) {
                    dst = i;
                    break;
                }
            }
            getDirectionAS(src, dst);
        }

        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        IconGenerator iconFactory = new IconGenerator(this);
        if(origin!=null&&destination!=null) {
            iconFactory.setBackground(getDrawable(R.color.icon));
            iconFactory.setTextAppearance(R.style.iconGenText);

            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(origin)))
                    .position(placeToLatLng(origin))
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

            mMap.addMarker(markerOptions);
            MarkerOptions markerOptions1 = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(destination)))
                    .position(placeToLatLng(destination))
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            mMap.addMarker(markerOptions1);
        }
        // set new title to the MenuItem
        if(marker_flag)
            label.setTitle("Hide Marker");
        else
            label.setTitle("Show Marker");
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    private void showDefaultLocation() {
        LatLng main_gate = new LatLng(26.25002, 78.17634);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(main_gate));
    }
    int x=0;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                    x=1;
                }
                return;
            }
        }
    }

//    public void animateMarker(final Marker marker, final LatLng toPosition,
//                              final boolean hideMarker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = mMap.getProjection();
//        Point startPoint = proj.toScreenLocation(marker.getPosition());
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 500;
//        final Interpolator interpolator = new LinearInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * toPosition.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * toPosition.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                } else {
//                    if (hideMarker) {
//                        marker.setVisible(false);
//                    } else {
//                        marker.setVisible(true);
//                    }
//                }
//            }
//        });
//    }

//    boolean isMarkerRotating=false;
//    private void rotateMarker(final Marker marker, final float toRotation) {
//        if(!isMarkerRotating) {
//            final Handler handler = new Handler();
//            final long start = SystemClock.uptimeMillis();
//            final float startRotation = marker.getRotation();
//            final long duration = 2000;
//            final Interpolator interpolator = new LinearInterpolator();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    isMarkerRotating = true;
//                    long elapsed = SystemClock.uptimeMillis() - start;
//                    float t = interpolator.getInterpolation((float) elapsed / duration);
//                    float rot = t * toRotation + (1 - t) * startRotation;
//                    float bearing =  -rot > 180 ? rot / 2 : rot;
//                    marker.setRotation(bearing);
//                    if (t < 1.0) {
//                        // Post again 16ms later.
//                        handler.postDelayed(this, 16);
//                    } else {
//                        isMarkerRotating = false;
//                    }
//                }
//            });
//        }
//    }
}
