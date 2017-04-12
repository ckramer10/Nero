package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.WaterReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubmitQualityList extends AppCompatActivity {
    //UI and firebase imports
    private ListView reportList;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList array_of_reports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quality_list);
        reportList = (ListView) findViewById(R.id.submit_quality_listview);
        array_of_reports = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");

        //gets snapshot of current reports in database
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            WaterReport w = eachReport.getValue(WaterReport.class);
                            array_of_reports.add(w);
                            w.setKey(eachReport.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                SubmitQualityList.this,
                                android.R.layout.simple_list_item_1,
                                array_of_reports );

                        reportList.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );

        reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WaterReport clickedReport = (WaterReport) parent.getAdapter().getItem(position);
                SubmitQualityReport.reportAddress = clickedReport.getAddress();
                SubmitQualityReport.latitude = clickedReport.getLatitude();
                SubmitQualityReport.longitude = clickedReport.getLongitude();
                SubmitQualityReport.report = clickedReport;

                Context context = getApplicationContext();
                Intent intent = new Intent(context, SubmitQualityReport.class);
                context.startActivity(intent);
            }

        });

    }
    /**
     * method that uses google to pinpoint latlong position
     * @param context the current frame the user is on
     * @param strAddress the address used for the coordinates
     * @return the latlong coordinates
     */
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    /**
     * makes back button always go to quality report options
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SubmitQualityReportOptions.class);
        context.startActivity(intent);
    }
}
