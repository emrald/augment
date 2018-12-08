package com.trivediinfoway.theinnontheriver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by TI A1 on 05-10-2016.
 */
public class ActivityArea extends Activity implements AdapterView.OnItemClickListener, LocationListener {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    Dialog listDialog;
    ListView list1;
    ImageView imgbackarea,img;
    protected LocationManager locationManager;

    static String strName = "";
    String[] array_km = {"1 Miles", "2 Miles", "3 Miles", "4 Miles", "5 Miles", "6 Miles",
            "7 Miles", "8 Miles", "9 Miles", "10 Miles","11 Miles","12 Miles","13 Miles"
            ,"14 Miles","15 Miles","16 Miles","17 Miles","18 Miles","19 Miles","20 Miles"};

    ListView list;
    String[] list_array = {"Accounting", "Airport", "Amusement Park", "Aquarium", "Art Gallery", "ATM",
            "Bakery", "Bank", "Bar", "Beauty Salon", "Bicycle Store", "Book Store", "Bowling Alley", "Bus Station",
            "Cafe", "Campground", "Car Dealer", "Car Rental", "Car Repair", "Car Wash", "Casino", "Cemetery", "Church",
            "City Hall", "Clothing Store", "Convenience Store", "Courthouse",
            "Dentist", "Department Store", "Doctor",
            "Electrician", "Electronics Store", "Embassy", "Fire Station", "Florist", "Funeral Home", "Furniture Store",
            "Gas Station", "Gym", "Hair Care", "Hardware Store", "Hindu Temple", "Home Goods Store", "Hospital",
            "Insurance Agency", "Jewelry Store", "Laundry", "Lawyer", "Library", "Liquor Store", "Local Government Office",
            "Locksmith", "Lodging", "Meal Delivery", "Meal Takeaway", "Mosque", "Movie Rental", "Movie Theater",
            "Moving Company", "Museum", "Night Club",
            "Painter", "Park", "Parking", "Pet Store", "Pharmacy", "Physiotherapist", "Plumber", "Police", "Post Office",
            "Real Estate Agency", "Restaurant", "Roofing Contractor", "RV Park",
            "School", "Shoe Store", "Shopping Mall", "Spa", "Stadium", "Storage", "Store", "Subway Station", "Synagogue",
            "Taxi Stand", "Train Station", "Transit Station", "Travel Agency", "University", "Veterinary Care", "Zoo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_business);

        list = (ListView) findViewById(R.id.list);
        imgbackarea = (ImageView) findViewById(R.id.imgbackarea);
        img = (ImageView)findViewById(R.id.img);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        locationManager = (LocationManager) ActivityArea.this
                .getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
            //     return;

        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }
        }

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityArea.this, R.layout.row, R.id.label, list_array);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (ActivityArea.this, R.layout.row, R.id.label, list_array){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 0)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#F6EFE4"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                return view;
            }
        };

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
               // if()
                Intent intent = new Intent(ActivityArea.this, MapActivity.class);
                //  String message = "abc";
                String replace = parent.getItemAtPosition(position).toString().replace("_", "");
                StringBuilder sb = new StringBuilder(replace);
                for (int index = 0; index < sb.length(); index++) {
                    char c = sb.charAt(index);
                    sb.setCharAt(index, Character.toLowerCase(c));
                }
                intent.putExtra("category", sb.toString());
                intent.putExtra("strName", strName+"");
                intent.putExtra("placename", list_array[position].toString());

                /*Log.e("str...", sb.toString()+":CAT");
                Log.e("strName...", strName + ":NAME");*/
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Dialog dialog = new Dialog(ActivityArea.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //   dialog.getWindow().setBackgroundDrawableResource(R.color.colorAccent);
                dialog.setContentView(R.layout.dialog_km);

                ListView listkm = (ListView) dialog.findViewById(R.id.listkm);
                Button btnok = (Button) dialog.findViewById(R.id.btnok);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityArea.this, R.layout.row_km, R.id.tvkm, array_km)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                    // Get the current item from ListView
                    View view = super.getView(position,convertView,parent);
                    if(position %2 == 0)
                    {
                        // Set a background color for ListView regular row/item
                        view.setBackgroundColor(Color.parseColor("#F6EFE4"));
                    }
                    else
                    {
                        // Set the background color for alternate row/item
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    return view;
                }
                };

                listkm.setAdapter(adapter);

                listkm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        strName = (String) parent.getItemAtPosition(position);
                        Toast.makeText(ActivityArea.this,"You have selected "+strName,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                     //   view.setBackgroundColor(Color.parseColor("#ffe4ee"));
                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }

        });
        imgbackarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityArea.this);
        builder.setPositiveButton("OK ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("OK CLICKED");
                strName = (String) list1.getItemAtPosition(arg2);
                dialog.dismiss();
                listDialog.cancel();

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                listDialog.cancel();
            }
        });
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        //  mapFrag.getMapAsync(MainActivity.this);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }

            return false;
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();
            }

            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityArea.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        ActivityArea.this.finish();
    }

    @Override
    public void onLocationChanged(Location location) {

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
}

