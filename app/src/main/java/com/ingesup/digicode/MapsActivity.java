package com.ingesup.digicode;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Context;
import android.os.Build;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static String Digicode = "codeKey";
    Map<String, String> digicodes = new HashMap<String, String>();
    double latitude = 0;
    double longitude = 0;

    EditText ed1,ed3,edall,GmapsInfo;
    Button b1;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ed1=(EditText)findViewById(R.id.editText);
        ed3=(EditText)findViewById(R.id.editText3);
        edall=(EditText)findViewById(R.id.editText1);
        GmapsInfo=(EditText)findViewById(R.id.GmapsInfo);
        b1=(Button)findViewById(R.id.button);
        //
        StringBuilder array = new StringBuilder(100);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loadArray();
       // char[] array = new char[0];
        if(MyPREFERENCES!=null){
            edall.setText(MyPREFERENCES);
        }else
            System.out.println("Array is not initialized or empty");

        Map<String, ?> allEntries = sharedpreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            array.append("Nom : "+entry.getKey() + "  | GPS : " + entry.getValue().toString()+"\n");
        }

        //sharedpreferences.edit().clear().commit();
        String value1 = sharedpreferences.getString("Company", "");
        ed1.setText(value1);
        String value3 = sharedpreferences.getString("Digicode", "");
        ed3.setText(value3);
        edall.setText(array);
       // edall.setText("Votre dernière sauvegarde : \n"+"Société -> "+value1+"\n"+"Etage->"+"\n"+"Digicode->"+value3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  addDigicode();
                String n  = ed1.getText().toString();
                String e  = ed3.getText().toString();
                String tmp = (n+"| Code :"+e);
                double n1 = latitude;
                double n2 = longitude;
                String n1toString = String.valueOf(n1);
                String n2toString = String.valueOf(n2);
                String tmpGPS = (n1toString+"/"+n2toString);
                digicodes.put(tmp, tmpGPS);

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedpreferencesEditor = sharedpreferences.edit();

                for (String s : digicodes.keySet()) {
                    // use the name as the key, and the icon as the value
                    sharedpreferencesEditor.putString(s, digicodes.get(s));
                }
                sharedpreferencesEditor.commit();
                loadDatasDigi();

                /////////////////////// AVEC SHAREDPREFERENCE ////////////////////////////////
                //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                //String n  = ed1.getText().toString();
                //String ph  = ed2.getText().toString();
                //String e  = ed3.getText().toString();
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putString("Company", n);
                //editor.putString("InfoBuilding", ph);
                //editor.putString("Digicode", e);
                //editor.commit();
                Toast.makeText(MapsActivity.this,"Thanks !! ",Toast.LENGTH_LONG).show();
                //////////////////////////////////////////////////////////////////////////////
            }
            // private void addDigicode() {
            //    String v1 = ed1.getText().toString();
            //    String v2 = ed2.getText().toString();
            //   String v3 = ed3.getText().toString();
            //    DataDigicode newdigicodes1 = new DataDigicode(v1, v2, v3);
            //    newdigicodes1.save();


            //  }
        });

    }

    public void loadDatasDigi() {
        StringBuilder array = new StringBuilder(100);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedpreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            array.append("Nom : "+entry.getKey() + "  | GPS : " + entry.getValue().toString()+"\n");
        }
        edall.setText(array);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    public String[] loadArray() {
     //   getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int size = sharedpreferences.getInt(digicodes + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = sharedpreferences.getString(digicodes + "_" + i, null);
        return array;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Location locationOne = new Location("");
        locationOne.setLatitude(48.85115353);
        locationOne.setLongitude(2.2673489);
        float distanceInMetersOne = locationOne.distanceTo(location);
        String distanceInMetersOneLocToOther = String.valueOf(distanceInMetersOne);
        //Log.d("VALEUR", "Value: " + Float.toString(distanceInMetersOne));
        Toast.makeText(MapsActivity.this,"Vous êtes à "+distanceInMetersOneLocToOther+" mètres d'une de vos saisies",Toast.LENGTH_LONG).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        GmapsInfo.setText("Latitude:" + latitude + ", Longitude:" + longitude);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        SharedPreferences mSettings = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        //Company = mSettings.getString("Company", Company);
        //InfoBuilding = mSettings.getString("InfoBuilding", InfoBuilding);
        Digicode = mSettings.getString("Digicode", Digicode);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}