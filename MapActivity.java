package com.trivediinfoway.theinnontheriver;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class MapActivity extends FragmentActivity implements LocationListener {

    GoogleMap mGoogleMap;
    //	Spinner mSprPlaceType;

    //String[] mPlaceType=null;
    //String[] mPlaceTypeName=null;

    ProgressDialog pd;

    static double mLatitude = 0;
    static double mLongitude = 0;

    HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

    Button btncamera;
    CustomBusinessListAdapter adapter;

    static ArrayList<Double> lat_list = null;
    static ArrayList<Double> long_list = null;
    static ArrayList<String> place_name_list = null;
    static ArrayList<String> reference_list = null;
    static ArrayList<String> vicinity_list;
    Bundle bn;
    String category = "";
    String strName = "";
    //TextView tvplacename, tvlist, tvmap, tvnorecords;
    TextView tvplacename,tvlist, tvmap;
    String placename="";
    RelativeLayout rlupper, rlmap;
    ListView listbusiness;
    ArrayList<BusinessDataClass> arraylist;
    String phnumber = "";
    String webaddress = "";
    ArrayList<String> phnumber_list;
    ArrayList<String> webaddress_list;
    LinearLayout defaultimagemap;
    ImageView imgbackmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MapActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
        }

        btncamera = (Button) findViewById(R.id.btncamera);
        tvlist = (TextView) findViewById(R.id.tvlist);
        tvmap = (TextView) findViewById(R.id.tvmap);
        imgbackmap = (ImageView)findViewById(R.id.imgbackmap);
     //   tvnorecords = (TextView) findViewById(R.id.tvnorecords);
        tvplacename = (TextView)findViewById(R.id.tvplacename);

        listbusiness = (ListView) findViewById(R.id.listbusiness);

        rlupper = (RelativeLayout) findViewById(R.id.rlupper);
        rlmap = (RelativeLayout) findViewById(R.id.rlmap);
        defaultimagemap = (LinearLayout)findViewById(R.id.defaultimagemap);

        rlmap.setVisibility(View.GONE);
        rlupper.setVisibility(View.VISIBLE);

//        tvnorecords.setVisibility(View.GONE);
        defaultimagemap.setVisibility(View.GONE);

        bn = getIntent().getExtras();
        category = bn.getString("category");
        category = category.replace(" ", "_");

        strName = bn.getString("strName");
        strName = strName.replace(" Miles", "");

        placename = bn.getString("placename");
        tvplacename.setText(placename+"");

        tvlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlmap.setVisibility(View.GONE);
                listbusiness.setVisibility(View.VISIBLE);

                tvmap.setTextColor(Color.parseColor("#ffffff"));
                tvlist.setTextColor(Color.parseColor("#243036"));

                tvmap.setBackgroundColor(Color.parseColor("#243036"));
                tvlist.setBackgroundColor(Color.parseColor("#ffffff"));

                //  try{
                try {
                    if (place_name_list.size() == 0) {
                        // tvnorecords.setVisibility(View.VISIBLE);
                        defaultimagemap.setVisibility(View.VISIBLE);
                    } else {
                        defaultimagemap.setVisibility(View.GONE);
                        //tvnorecords.setVisibility(View.GONE);
                    }
                }
                catch (Exception e)
                {
                    Log.e("Size 0",e.getMessage()+"");
                }
           /* }
                catch (Exception e)
                {
                    Log.e("Size 0",e.getMessage()+"");
                }*/
            }
        });
        tvmap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlmap.setVisibility(View.VISIBLE);
                listbusiness.setVisibility(View.GONE);

                tvlist.setTextColor(Color.parseColor("#ffffff"));
                tvmap.setTextColor(Color.parseColor("#243036"));

                tvlist.setBackgroundColor(Color.parseColor("#243036"));
                tvmap.setBackgroundColor(Color.parseColor("#ffffff"));

             //   tvnorecords.setVisibility(View.GONE);
                defaultimagemap.setVisibility(View.GONE);
            }
        });
        imgbackmap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Array of place types

        // Getting Google Play availability status
        try {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

            if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();

            } else { // Google Play Services are available

                // Getting reference to the SupportMapFragment
                SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                // Getting Google Map
                mGoogleMap = fragment.getMap();

                // Enabling MyLocation in Google Map
                mGoogleMap.setMyLocationEnabled(true);
                status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapActivity.this);

                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider

                String provider = null;
                Location location = null;
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                    try {
                        //     Log.e("NETWORK_PROVIDER provider", "NETWORK_PROVIDER...");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        provider = locationManager.getBestProvider(new Criteria(), true);
                    } catch (Exception ex) {
                        Log.e("NETWORK_PROVIDER Error", ex.getMessage() + "");
                    }
                } else {
                    try {
                        //          Log.e("GPS_PROVIDER provider", "GPS_PROVIDER...");
                        provider = locationManager.getBestProvider(new Criteria(), true);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                        Log.e("GPS_PROVIDER Error", ex.getMessage() + "");
                    }
                }
                if (location != null) {

                    onLocationChanged(location);
                }

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
                locationManager.requestLocationUpdates(provider, 20000, 0, this);

                mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {
                        Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
                        String reference = mMarkerPlaceLink.get(arg0.getId());
                        intent.putExtra("reference", reference);

                        // Starting the Place Details Activity
                        startActivity(intent);
                    }
                });

                //Log.e("Lat and Long...", mLatitude + "," + mLongitude);

                if(isNetworkAvailable()) {
                    if (strName.equals("")) {
                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("location=" + mLatitude + "," + mLongitude);
                        sb.append("&radius=" + 1609.34);
                        sb.append("&types=" + category);
                        sb.append("&sensor=true");
                        //	sb.append("&key=AIzaSyAhATo-37y3UW8oEQvA1Muze0UUdNiUbv0");
                        sb.append("&key=AIzaSyCi0A4qY_eTvX04kkkqehk6JwqPg3cSX14");
                        PlacesTask placesTask = new PlacesTask();

                        // Invokes the "doInBackground()" method of the class PlaceTask
                        placesTask.execute(sb.toString());

                        //Log.e("sb.toString()", sb.toString() + "");
                    } else {
                        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        sb.append("location=" + mLatitude + "," + mLongitude);
                        sb.append("&radius=" + Integer.parseInt(strName) * 1609.34);
                        sb.append("&types=" + category);
                        sb.append("&sensor=true");
                        //	sb.append("&key=AIzaSyAhATo-37y3UW8oEQvA1Muze0UUdNiUbv0");
                        sb.append("&key=AIzaSyCi0A4qY_eTvX04kkkqehk6JwqPg3cSX14");
                        PlacesTask placesTask = new PlacesTask();

                        // Invokes the "doInBackground()" method of the class PlaceTask
                        placesTask.execute(sb.toString());

                        //Log.e("sb.toString()", sb.toString() + "");
                    }
                }
                else
                {
                    Toast.makeText(MapActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Error...",e.getMessage()+"");
        }
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
            Log.d("Exception...", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }


    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show(MapActivity.this, "", "Loading...");
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }
            try {
                ParserTask parserTask = new ParserTask();
                // Start parsing the Google places in JSON format
                // Invokes the "doInBackground()" method of the class ParseTask
                parserTask.execute(result);
            }
            catch(Exception e)
            {
                Log.e("Error...",e.getMessage()+"");
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            lat_list = new ArrayList<Double>();
            long_list = new ArrayList<Double>();
            place_name_list = new ArrayList<String>();
            reference_list = new ArrayList<String>();
            vicinity_list = new ArrayList<String>();
            arraylist = new ArrayList<BusinessDataClass>();

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
          //  pd.dismiss();
            mGoogleMap.clear();
            try {
                for (int i = 0; i < list.size(); i++) {

                    BusinessDataClass data = new BusinessDataClass();
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);

                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));
                    lat_list.add(lat);
                    data.setLatitude(lat);

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));
                    long_list.add(lng);
                    data.setLongitude(lng);

                    // Getting name
                    String name = hmPlace.get("place_name");
                    place_name_list.add(name);
                    data.setBusinessname(name);

                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");
                    data.setAddress(vicinity);
                    //Log.e("vicinity...", vicinity + "");
                    //		vicinity_list.add(vicinity);
                    String reference = hmPlace.get("reference");
                    reference_list.add(reference);
                    data.setReference(reference);

					/*HashSet<String> hashSet_vicinity = new HashSet<String>();
                    hashSet_vicinity.addAll(vicinity_list);
					vicinity_list.clear();
					vicinity_list.addAll(hashSet_vicinity);

					HashSet<Double> hashSet_lat = new HashSet<Double>();
					hashSet_lat.addAll(lat_list);
					lat_list.clear();
					lat_list.addAll(hashSet_lat);

					HashSet<Double> hashSet_long = new HashSet<Double>();
					hashSet_long.addAll(long_list);
					long_list.clear();
					long_list.addAll(hashSet_long);

					HashSet<String> hashSet_place = new HashSet<String>();
					hashSet_place.addAll(place_name_list);
					place_name_list.clear();
					place_name_list.addAll(hashSet_place);

					HashSet<String> hashSet_reference = new HashSet<String>();
					hashSet_reference.addAll(reference_list);
					reference_list.clear();
					reference_list.addAll(hashSet_reference);
*/
                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    //This will be displayed on taping the marker
                    markerOptions.title(name + " : " + vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));

                    // Placing a marker on the touched position
                    Marker m = mGoogleMap.addMarker(markerOptions);

                    // Linking Marker id and place reference
                    mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
                    arraylist.add(data);

                }
                if (place_name_list.size() == 0) {
                  //  tvnorecords.setVisibility(View.VISIBLE);
                    defaultimagemap.setVisibility(View.VISIBLE);
                }
                else {
                    adapter = new CustomBusinessListAdapter(MapActivity.this, arraylist);
                    listbusiness.setAdapter(adapter);
                }
            } catch (Exception e) {

                Log.e("Error...", e.getMessage() + "");
            }
            btncamera.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (lat_list.size() == 0) {
                        Toast.makeText(MapActivity.this, "No Records found.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MapActivity.this, Compass.class);
                        MapActivity.this.startActivity(intent);
                    }
                }
            });
        }

    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 14.0f), 4000, null);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((pd != null) && pd.isShowing())
            pd.dismiss();
        pd = null;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}