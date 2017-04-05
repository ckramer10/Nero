package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.sffilps.waterlocater.model.WaterReport;


/**
 * Created by ckramer on 4/2/17.
 */

public class HistoricalReportList extends AppCompatActivity {

    public static String reportAddress;
    public static double latitude;
    public static double longitude;
    public static WaterReport report;
    private ListView reportList;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList array_of_reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_report_list);
        reportList = (ListView) findViewById(R.id.historical_listview);
        array_of_reports = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");

        //gets snapshot of current reports in database
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            WaterReport w = eachReport.getValue(WaterReport.class);
                            array_of_reports.add(w);
                            w.setKey(eachReport.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                HistoricalReportList.this,
                                android.R.layout.simple_list_item_1,
                                array_of_reports);

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
                HistoricalReportSettings.reportAddress = clickedReport.getAddress();
                HistoricalReportSettings.latitude = clickedReport.getLatitude();
                HistoricalReportSettings.longitude = clickedReport.getLongitude();
                HistoricalReportSettings.report = clickedReport;

                Context context = getApplicationContext();
                Intent intent = new Intent(context, HistoricalReportSettings.class);
                context.startActivity(intent);
            }
        });




    }

}
