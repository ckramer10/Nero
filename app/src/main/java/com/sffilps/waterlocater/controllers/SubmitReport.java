package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ckramer on 2/23/17.
 */

public class SubmitReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button submitButton;
    private Button cancelButton;
    private Spinner typeSpinner;
    private Spinner conditionSpinner;
    private int counter;
    private String stringCount;
    private String userName;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String type;
    private String condition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        submitButton = (Button) findViewById(R.id.submitReportButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        List<String> types = new ArrayList<String>();
        types.add("Bottled");
        types.add("Well");
        types.add("Stream");
        types.add("Lake");
        types.add("Spring");
        types.add("Other");

        List<String> conditions = new ArrayList<String>();
        conditions.add("Waste");
        conditions.add("Treatable-Clear");
        conditions.add("Treatable-Muddy");
        conditions.add("Potable");

        ArrayAdapter<String> typesDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        typesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> conditionsDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conditions);
        conditionsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(typesDataAdapter);
        conditionSpinner.setAdapter(conditionsDataAdapter);

        typeSpinner.setSelection(0);
        conditionSpinner.setSelection(0);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitReport()) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, HomeScreen.class);
                    context.startActivity(intent);
                    Toast.makeText(SubmitReport.this, "Water Report Submitted",Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            }
        });

        if (currentUser != null) {
            final String uID = currentUser.getUid();
            mDatabase.child(uID).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            setUserName((String) dataSnapshot.child("name").getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        mDatabase.child("Reports").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        setCount(dataSnapshot.child("count").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                type = "Bottled";

            }
        });

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                condition = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                condition = "Potable";

            }
        });
    }

    private boolean submitReport() {
        WaterReport newReport = new WaterReport(condition,type,userName,"Null");
        DatabaseReference usersRef = mDatabase.child("Reports");
        WaterReport report = new WaterReport(condition,type,userName,"Location");
        usersRef.child(stringCount).setValue(report);
        usersRef.child("count").setValue(stringCount);
        return true;
    }

    public void setUserName(String s) {
        userName = s;
    }

    public void setCount(String i) {
        stringCount = i;
        counter = Integer.parseInt(stringCount);
        counter++;
        stringCount = Integer.toString(counter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
