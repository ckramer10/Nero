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
 * Created by ckramer on 2/10/17.
 */

public class HomeScreen extends AppCompatActivity {

    private Button signoutButton;
    private Button submitReport;
    private Button viewReports;
    private FirebaseAuth mAuth;
    private TextView name;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        signoutButton = (Button) findViewById(R.id.signout_button);
        submitReport = (Button) findViewById(R.id.submitreport);
        viewReports = (Button) findViewById(R.id.viewwsources);
        name = (TextView) findViewById(R.id.email_text);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                Context context = v.getContext();
                Intent intent = new Intent(context, SplashScreen.class);
                context.startActivity(intent);
                Toast.makeText(HomeScreen.this, "Signed Out.", Toast.LENGTH_LONG).show();
            }
        });

        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,SubmitReport.class);
                startActivity(i);
            }
        });

        if (currentUser != null) {
            String email = currentUser.getEmail();
            final String uID = currentUser.getUid();
            mDatabase.child(uID).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            setUserName((String) dataSnapshot.child("name").getValue());
                            name.setText("Welcome, " + userName);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            name.setText("Didn't work");
        }

    }



    public void setUserName(String s) {
        userName = s;
    }
    

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
