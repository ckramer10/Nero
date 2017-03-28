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
import com.sffilps.waterlocater.model.PurityReport;
import com.sffilps.waterlocater.model.WaterReport;

import java.util.ArrayList;

public class PurityReportListView extends AppCompatActivity {
    private ListView purityReportList;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList array_of_reports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_report_list_view);

        purityReportList = (ListView) findViewById(R.id.purity_report_list);
        array_of_reports = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");

        //gets snapshot of current purity reports in database
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot eachReport : dataSnapshot.getChildren()) {
                            WaterReport w = eachReport.getValue(WaterReport.class);
                            for (PurityReport pr : w.getPurityList())
                            array_of_reports.add(pr);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                PurityReportListView.this,
                                android.R.layout.simple_list_item_1,
                                array_of_reports );

                        purityReportList.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //do nothing
                    }
                }
        );
    }
}
