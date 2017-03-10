package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.identity.intents.Address;
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
import java.util.Map;
import java.util.Objects;

public class ReportListView extends AppCompatActivity {
    //UI and firebase imports
    private ListView reportList;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList array_of_reports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list_view);
        reportList = (ListView) findViewById(R.id.report_list);
        array_of_reports = new ArrayList();

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

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                ReportListView.this,
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

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
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
}
