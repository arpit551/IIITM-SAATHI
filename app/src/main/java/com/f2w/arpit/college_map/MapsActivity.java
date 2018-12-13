package com.f2w.arpit.college_map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener , NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    String origin, destination;
    private LocationManager locationManager;
    Button start, stop;
    Boolean flag1 = false;
    int flag2 = 2;
    String[] locations = {"Main Gate", "Administrative Block", "Learning Resource Center (LRC)", "New Auditorium", "Cafeteria Canteen", "Cricket Ground", "A-Block", "B-Block/LT-1", "C-Block", "D-Block", "E-Block", "F-Block", "G-Block", "H-Block", "Hospital", "Open Air Theatre (OAT)", "Sports Complex"};

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        tvDistanceDuration = (TextView) findViewById(R.id.text);
        origin = getIntent().getStringExtra("Origin");
        destination = getIntent().getStringExtra("Destination");
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag1 = true;
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag2 = 1;
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&mode="+"walking"+"&key="



                +
                "AIzaSyD8yEcLQCWLZKgkJpxYzgssSxH-Msed7Tw" ;
        Log.d("url", url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception ", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        Log.d("data12", data);

        return data;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        if (id == R.id.hybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // Handle the camera action
        } else if (id == R.id.satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (id == R.id.terrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {if(flag1==true &&flag2==2) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("data12", String.valueOf((location.getLatitude()))
        );
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18.0f);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }
    else
    {
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.249994, 78.176121),17));
    }
//move map camera
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();





        if (id == R.id.nav_camera) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.nav_slideshow) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (id == R.id.nav_manage) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        } else if (id == R.id.arp) {

        } else if (id == R.id.pra){

        }
         else if (id == R.id.ari){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("data", data);
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

            Log.d("result", result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";


            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.RED);
            }

            tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /*
    me
     */
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
            poly.add(p);
        }
        return poly;
    }
    private  LatLng  placeToLatLng(String place){
        if(place=="Main Gate")
        {

            LatLng main_gate = new LatLng(26.25002, 78.17634);
            return main_gate;
        }
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
            p.color(Color.BLUE);

        }
        mMap.addPolyline(p);

    }

  String[][][] p =
            {{	{"0",""},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"1" , "_~e_D_zc|MTzAEBCBCBADAF?F@D@BBDBBB@B@B@dA|GHDHGH^"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"1",""},	{"2","_ze_Dkkc|MfAhH"},	{"3","_ze_Dkkc|Mb@xCfAWDAD@D@B?~@Y"},	{"2","_ze_Dkkc|MfAhH"},	{"2","_ze_Dkkc|MfAhH"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@c@wC"},	{"14","_ze_Dkkc|M@FcBZQ@ODIJCNEPe@JBV"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"2",""},	{"3","}we_Dcbc|M]mCfAWDAD@D@B?~@Y"},	{"4","wwe_Dabc|My@HB`@]H"},	{"5","wwe_Dabc|My@H@`@H`@x@hB"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"7","wwe_Dabc|Me@mCMRWRQHg@De@YUj@"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"3",""},	{"2","}we_Dcbc|M]mCfAWDAD@D@B?~@Y"},	{"5","}se_Dahc|M?F?D@BBBBBD?BAHZTEPFiCpGGb@MB"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"7","y}e_Dcdc|MRg@b@RPCXAd@WP_@~Ck@"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"4",""},	{"5","ize_Dm`c|MLCtAbC"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"7","y}e_Dcdc|MRg@b@RVEHJGl@Fl@Rf@"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"5",""},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"4","ize_Dm`c|MLCtAbC"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"6",""},	{"7","y}e_Dcdc|M_@SEO"},	{"7","y}e_Dcdc|M_@SEO"},	{"13","uaf_Dmdc|MtAY"},	{"13","uaf_Dmdc|MtAY"},	{"13","uaf_Dmdc|MtAY"},	{"13","uaf_Dmdc|MtAY"},	{"13","uaf_Dmdc|MtAY"},	{"14","__f_Deec|MCQGBKy@"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"7",""},	{"8","y}e_Dcdc|MS\\@P"},	{"8","y}e_Dcdc|MS\\@P"},	{"8","y}e_Dcdc|MS\\@P"},	{"8","y}e_Dcdc|MS\\@P"},	{"6","y}e_Dcdc|M_@SEO"},	{"6","y}e_Dcdc|M_@SEO"},	{"6","y}e_Dcdc|M_@SEO"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"8",""},	{"9","i~e_Dubc|MwAZ"},	{"9","i~e_Dubc|MwAZ"},	{"9","i~e_Dubc|MwAZ"},	{"9","i~e_Dubc|MwAZ"},	{"9","i~e_Dubc|MwAZ"},	{"7","y}e_Dcdc|MS\\@P"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"9",""},	{"10","ecf_Deac|MbAS"},	{"10","ecf_Deac|MbAS"},	{"13","aaf_Dyac|MSqA"},	{"13","aaf_Dyac|MSqA"},	{"13","aaf_Dyac|MSqA"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"10",""},	{"11","ecf_Deac|MWFCQYQ"},	{"11","ecf_Deac|MWFCQYQ"},	{"9","ecf_Deac|MbAS"},	{"9","ecf_Deac|MbAS"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"11",""},	{"12","{cf_Dwcc|MSD@NK\\"},	{"12","{cf_Dwcc|MSD@NK\\"},	{"12","{cf_Dwcc|MSD@NK\\"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"12",""},	{"13","uaf_Dmdc|MgAT"},	{"13","uaf_Dmdc|MgAT"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"13",""},	{"6","uaf_Dmdc|MtAY"},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"14",""},	{"0",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"15",""},	{"0",""}	},
{	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"0",""},	{"16",""}	}
}
    ;
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
//        placeToLatLng(origin);
//        placeToLatLng(dest);
//        if(origin.equals("Main Gate ") )
//        {
//                if(dest.equals("Administrative Block")) {
//                    List<LatLng> poly = decodePoly1(path[0][0][1]);
//                    pathDrawn(poly);
//                }
//                if(dest.equals("Learning Resource Center")) {
//                    List<LatLng> poly = decodePoly1("cze_Dskc|MJz@l@zD");
//
//                    pathDrawn(poly);
//                    getDirectionAS("Main Gate ","Administrative Block");
//
//                }
//                 if(dest.equals("B-Block/LT-1")) {
//
//                getDirectionAS("Main Gate ","Administrative Block");
//                     getDirectionAS("Administrative Block","B-Block/LT-1");
//                }
//                if(dest.equals("New Auditorium")) {
//
//                getDirectionAS("Main Gate ","Administrative Block");
//                getDirectionAS("Administrative Block","New Auditorium");
//                }
//
//
//       }





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerPoints = new ArrayList<LatLng>();
        // Enable MyLocation Button in the Map

        // Setting onclick event listener for the map
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng point) {
//
//                // Already two locations
//                if (markerPoints.size() > 1) {
//                    markerPoints.clear();
//                    mMap.clear();
//                }
//
//                // Adding new item to the ArrayList
//                markerPoints.add(point);
//
//                // Creating MarkerOptions
//                MarkerOptions options = new MarkerOptions();
//
//                // Setting the position of the marker
//                options.position(point);
//
//                /**
//                 * For the start location, the color of marker is GREEN and
//                 * for the end location, the color of marker is RED.
//                 */
//                if (markerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (markerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//
//                // Add new marker to the Google Map Android API V2
//                mMap.addMarker(options);
//
//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
//                    LatLng origin = markerPoints.get(0);
//                    LatLng dest = markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
//                }
//            }
//        });
////location
//        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
//        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();




        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(26.249994, 78.176121);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
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
       LatLng lt2 = new LatLng(26.249759, 78.172947);
//        mMap.setLatLngBoundsForCameraTarget(bounds);
       mMap.addMarker(new MarkerOptions()
               .position(lt2)
                .title("LT-2")
               .icon(BitmapDescriptorFactory.fromResource(R.drawable.exhibition_map)));
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.249994, 78.176121),17));
//        //move camera to fill the bound to screen
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
//
        int src = -1, dst = -1;
        int n = locations.length;
        for(int i = 0; i < n ; i++){
            if(locations[i].equals(origin) || locations[i].equals(destination)){
                src = i;
                break;
            }
        }
        for(int i = src + 1; i < n ; i++){
            if(locations[i].equals(origin) || locations[i].equals(destination)){
                dst = i;
                break;
            }
        }
        getDirectionAS(src, dst);
//        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
//        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);
//        mMap.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                        this, R.raw.style_json));
        ArrayList<LatLng> points = null;
        PolylineOptions audi_lrc= null;
        points = new ArrayList<LatLng>();
        audi_lrc = new PolylineOptions();
        double lat = 26.249182;
        double lng =78.172871;
        LatLng position = new LatLng(lat, lng);

        points.add(position);
        double lat1 = 26.249428;
        double lng1 =78.174073;
        LatLng position1 = new LatLng(lat1, lng1);

        points.add(position1);
        audi_lrc.addAll(points);
        audi_lrc.width(16);
        audi_lrc.color(Color.WHITE);

       // mMap.addPolyline(audi_lrc);



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
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

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
                }
                return;
            }

        }
    }

//    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
//            new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//                    mMap.setMinZoomPreference(15);
//                    return false;
//                }
//            };

//    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
//            new GoogleMap.OnMyLocationClickListener() {
//                @Override
//                public void onMyLocationClick(@NonNull Location location) {
//
//                    mMap.setMinZoomPreference(12);
//
//                    CircleOptions circleOptions = new CircleOptions();
//                    circleOptions.center(new LatLng(location.getLatitude(),
//                            location.getLongitude()));
//
//                    circleOptions.radius(200);
//                    circleOptions.fillColor(Color.RED);
//                    circleOptions.strokeWidth(6);
//
//                    mMap.addCircle(circleOptions);
//                }
//            };
}
