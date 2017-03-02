package com.sffilps.waterlocater.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Map;

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
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        for( Map.Entry<String,Object> w : map.entrySet()) {

                            array_of_reports.add(w.getValue());
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
}
