package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sffilps.waterlocater.R;

/**
 * Created by ckramer on 3/12/17.
 */

public class HomeScreenWorker extends AppCompatActivity {
    private Button submitReport;
    private Button viewReportsList;
    private Button viewReportsMap;
    private Button viewQualityReports;
    private Button settings;
    private FirebaseAuth mAuth;
    private TextView name;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home_worker);

        settings = (Button) findViewById(R.id.settings);
        submitReport = (Button) findViewById(R.id.submitreport);
        viewReportsList = (Button) findViewById(R.id.viewwsourceslist);
        viewReportsMap = (Button) findViewById(R.id.viewwsourcesmap);
        viewQualityReports = (Button) findViewById(R.id.viewQualityReports);
        name = (TextView) findViewById(R.id.email_text);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Settings.class);
                context.startActivity(intent);
            }
        });

        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SubmitReport.class);
                context.startActivity(intent);
            }
        });

        viewReportsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ReportListView.class);
                context.startActivity(intent);
            }
        });

        viewReportsMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ReportMapView.class);
                context.startActivity(intent);
            }
        });

        viewQualityReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, QualityReportHome.class);
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
                            String s = "Welcome, " + userName;
                            name.setText(s);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            String st = "Didn't work";
            name.setText(st);
        }

    }


    /**
     * A method to set the user name
     */
    private void setUserName(String s) {
        userName = s;
    }


    /**
     * method to ask user if they really want to sign out
     */
    public void onBackPressed(){
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, LoginScreen.class);
                        context.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to sign out?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
