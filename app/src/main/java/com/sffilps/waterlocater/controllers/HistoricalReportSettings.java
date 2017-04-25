package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.WaterReport;

import java.util.ArrayList;
import java.util.List;

import static com.sffilps.waterlocater.R.id.conditionSpinner;

/**
 * Created by ckramer on 4/2/17.
 */

public class HistoricalReportSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String reportAddress;
    public static double latitude;
    public static double longitude;
    public static WaterReport report;
    private String xText;
    private String yText;
    private Spinner xaxis;
    private Spinner yaxis;
    private TextView address;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button showGraphButton;
    private Button cancelButton;
    List<String> xOption;
    List<String> yOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_report_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        address = (TextView) findViewById(R.id.textView4);

        String s = "Viewing Historical Report at: \n" + report.getAddress();

        address.setText(s);
        showGraphButton = (Button) findViewById(R.id.showGraphButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        xaxis = (Spinner) findViewById(R.id.xspinner);
        yaxis = (Spinner) findViewById(R.id.yspinner);

        xOption = new ArrayList<String>();
        xOption.add("2014");
        xOption.add("2015");
        xOption.add("2016");
        xOption.add("2017");

        yOption = new ArrayList<String>();
        yOption.add("Contaminant");
        yOption.add("Virus");

        xaxis.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        yaxis.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);


        ArrayAdapter<String> xDataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, xOption);
        xDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xaxis.setAdapter(xDataAdapter);
        xaxis.setSelection(3);

        ArrayAdapter<String> yDataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, yOption);
        yDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yaxis.setAdapter(yDataAdapter);
        yaxis.setSelection(0);

        xaxis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                xText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                xText = "Contaminant";

            }
        });

        yaxis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                yText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                yText = "Contaminant";

            }
        });

        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HistoricalReportGraph.report = report;
                HistoricalReportGraph.xAxis = xText;
                HistoricalReportGraph.yAxis = yText;

                Context context = v.getContext();
                Intent intent = new Intent(context, HistoricalReportGraph.class);
                context.startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * makes back button always go to quality report options
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, HistoricalReportSplashScreen.class);
        context.startActivity(intent);
        Toast.makeText(this, "Historical Report Request Cancelled.",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
