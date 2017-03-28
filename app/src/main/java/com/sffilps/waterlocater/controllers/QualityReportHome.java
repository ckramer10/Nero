package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by ckramer on 3/12/17.
 */

public class QualityReportHome extends AppCompatActivity {

    private Button viewQualityReports;
    private Button submitQualityReport;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_report_home);

        viewQualityReports = (Button) findViewById(R.id.viewQualityReports);
        submitQualityReport = (Button) findViewById(R.id.submitQualityReport);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        //checks to make sure the user is a admin or manager
        viewQualityReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                final String uID = currentUser.getUid();
                mDatabase.child(uID).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                String role = ((String) dataSnapshot.child("role").getValue());
                                if (role.equals("Administrator") || role.equals("Manager")) {
                                    Intent intent = new Intent(QualityReportHome.this, PurityReportListView.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(QualityReportHome.this, "You must be a manager or admin for this function",Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }
        });

        submitQualityReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SubmitQualityReportOptions.class);
                context.startActivity(intent);
            }
        });

    }

    /**
     * makes back button direct to home screen worker
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, HomeScreenWorker.class);
        context.startActivity(intent);
    }
}
