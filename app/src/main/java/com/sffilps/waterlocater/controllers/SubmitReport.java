package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
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
    private EditText inputAddress;
    private Spinner conditionSpinner;
    private static int counter;
    private static String stringCount;
    private String userName;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mapName;
    private String type;
    private String condition;
    private boolean isNull;
    private View submit;
    private String count;
    private double currentLong;
    private double currentLat;
    private LatLng ll;
    private String role;
    private GoogleApiClient mGoogleApiClient;


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
        inputAddress = (EditText) findViewById(R.id.addressText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        isNull = false;

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
        conditionSpinner.setSelection(3);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit = v;
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
                Intent i = new Intent(SubmitReport.this,HomeScreen.class);
                Intent i2 = new Intent(SubmitReport.this,HomeScreenWorker.class);

                if (role.equals("Administrator") || role.equals("Manager") || role.equals("Worker")) {
                    startActivity(i2);
                } else {
                    startActivity(i);
                }
            }
        });

        final String uID = currentUser.getUid();
        mDatabase = mDatabase.child("Users");
        mDatabase.child(uID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        setUserName((String) dataSnapshot.child("name").getValue());
                        role = (String) dataSnapshot.child("role").getValue();
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

    /**
     * function to submit data to database
     * @return true if successful false if not
     */
    private boolean submitReport() {

        isNull = false;

        if (TextUtils.isEmpty(inputAddress.getText().toString().trim())) {
            Toast.makeText(this, "Please enter a valid address or POI before submitting.",Toast.LENGTH_LONG).show();
            return false;
        }

        ll = getLocationFromAddress(submit.getContext(),inputAddress.getText().toString().trim());

        if (isNull == true) {
            Toast.makeText(this, "Couldn't find address. Please try again.",Toast.LENGTH_LONG).show();
            return false;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Count").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        setCount(dataSnapshot.getValue().toString());
                        DatabaseReference usersRef = mDatabase.child("Reports");
                        currentLat = ll.latitude;
                        currentLong = ll.longitude;
                        WaterReport report = new WaterReport(condition,type,userName,mapName,currentLat,currentLong);
                        usersRef.child(stringCount).setValue(report);
                        System.out.println(report);
                        mDatabase.child("Count").setValue(stringCount);
                        return;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return true;
    }

    /**
     * sets the class variable from inside function
     * @param s name string
     */
    public void setUserName(String s) {
        userName = s;
    }

    /**
     * sets the count in database
     * @param i old count
     */
    public static String setCount(String i) {
        if (i.equals(null)) {
            throw(new NullPointerException("String is empty. Expected a number."));
        }

        try {
            stringCount = i;
            counter = Integer.parseInt(stringCount);
        } catch (IllegalArgumentException err) {
            throw(err);
        }

        counter++;
        stringCount = Integer.toString(counter);
        return stringCount;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     *
     * Gets Lat and Long from Address Entered
     * @param context Context of view
     * @param strAddress Address
     * @return Lat and Long of Address
     */
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null) {
                isNull = true;
                return null;
            }

            if (address.size() != 0) {
                android.location.Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                mapName = location.getFeatureName();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                isNull = true;
                return null;
            }


        } catch (IOException ex) {
            ex.printStackTrace();
            isNull = true;
            return null;
        }

        return p1;
    }
}
