package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class SubmitQualityReport extends AppCompatActivity {

    public static WaterReport report;
    public static Integer purityKey;
    private TextView address;
    public static String reportAddress;
    public static double longitude;
    public static double latitude;
    public Button submitQuality;
    public Button cancelQuality;
    public EditText virusPPM;
    public EditText contaminantPPM;
    public Spinner conditionSpinner;
    public String name;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quality_report);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        address = (TextView) findViewById(R.id.textView4);

        String s = "Submitting Quality Report at: " + report.getAddress();

        address.setText(s);
        submitQuality = (Button) findViewById(R.id.submit_quality_report);
        cancelQuality = (Button) findViewById(R.id.cancel_quality_report);
        conditionSpinner = (Spinner) findViewById(R.id.condition_spinner);
        virusPPM = (EditText) findViewById(R.id.virus_text_field);
        contaminantPPM = (EditText) findViewById(R.id.contaminant_text_field);

        List<String> conditions = new ArrayList<String>();
        conditions.add("Safe");
        conditions.add("Treatable");
        conditions.add("Untreatable");

        ArrayAdapter<String> conditionsDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditions);
        conditionsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionsDataAdapter);
        conditionSpinner.setSelection(0);

        submitQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uID = currentUser.getUid();
                mDatabase = mDatabase.child("Users");
                mDatabase.child(uID).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                name = ((String) dataSnapshot.child("name").getValue());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                purityKey = ((Integer.parseInt(dataSnapshot.child("Count").getValue().toString()))) + 1;
                                mDatabase.child("Count").setValue(purityKey.toString());
                                PurityReport purityReport = new PurityReport(purityKey, name, reportAddress, longitude,
                                        latitude, conditionSpinner.getItemAtPosition(conditionSpinner.getSelectedItemPosition()).toString(),
                                        virusPPM.getText().toString(), contaminantPPM.getText().toString());
                                report.addPurityReport(purityReport);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Reports").child(report.getKey()).setValue(report);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                Context context = v.getContext();
                Intent intent = new Intent(context, QualityReportHome.class);
                context.startActivity(intent);
                Toast.makeText(SubmitQualityReport.this, "Quality Report Submitted",Toast.LENGTH_SHORT).show();
            }
        });

        cancelQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
