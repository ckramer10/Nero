package com.sffilps.waterlocater.controllers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.WaterReport;

import java.util.ArrayList;

public class ReportMapView extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<WaterReport> array_of_reports;
    private Location mLastLocation;
    private LatLng ll;
    LocationRequest mLocationRequest;
    Location currentLocation;
    private final int TAG_CODE_PERMISSION_LOCATION = 1;
    Marker currLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        array_of_reports = new ArrayList<WaterReport>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");

        //gets snapshot of current reports in database
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        /*
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        for( Map.Entry<String,Object> w : map.entrySet()) {

                            array_of_reports.add(w.getValue());
                        }
                        */

                        for(DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            WaterReport w = eachReport.getValue(WaterReport.class);
                            array_of_reports.add(w);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );

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
        Toast.makeText(this,"onMapReady",Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }

        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            ll = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(ll);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }

        for (int i = 0; i < array_of_reports.size(); i++) {
            WaterReport reportToAdd = array_of_reports.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng markerLL = new LatLng(reportToAdd.getLatitude(),reportToAdd.getLongitude());
            markerOptions.position(markerLL);
            markerOptions.title(reportToAdd.getTitle());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection Suspended.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Failed.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        ll = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ll);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //currLocationMarker = mMap.addMarker(markerOptions);

        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,15));
    }
}
