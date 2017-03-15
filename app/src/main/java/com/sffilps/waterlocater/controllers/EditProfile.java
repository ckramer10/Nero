package com.sffilps.waterlocater.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.Person;
import com.sffilps.waterlocater.model.WaterReport;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    private Spinner spinner;
    private TextView nameView;
    private TextView emailView;
    private TextView addressView;
    private Button submit;

    private FirebaseAuth mAuth;
    private TextView name;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        spinner = (Spinner) findViewById(R.id.roleSpinner);
        nameView = (TextView) findViewById(R.id.editName);
        emailView = (TextView) findViewById(R.id.editEmail);
        addressView = (TextView) findViewById(R.id.editAddress);

        submit = (Button) findViewById(R.id.submit_changes);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        List<String> roles = new ArrayList<String>();
        roles.add("User (Default)");
        roles.add("Worker");
        roles.add("Manager");
        roles.add("Administrator");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        final String uID = currentUser.getUid();
        mDatabase.child(uID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Person currUser = dataSnapshot.getValue(Person.class);
                        nameView.setText(currUser.name);
                        emailView.setText(currUser.email);
                        addressView.setText(currUser.homeAddress);
                        switch(currUser.role) {
                            case "Administrator":
                                spinner.setSelection(3);
                                break;
                            case "Manager":
                                spinner.setSelection(2);
                                break;
                            case "Worker":
                                spinner.setSelection(1);
                                break;
                            default:
                                spinner.setSelection(0);
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
                progressDialog.setMessage("Submitting Changes...");
                progressDialog.show();
                String uID = currentUser.getUid();
                Person newUser = new Person(nameView.getText().toString(), emailView.getText().toString(),
                        spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString(),
                        uID, addressView.getText().toString());
                DatabaseReference currentUserDB = mDatabase.child(uID);
                currentUserDB.setValue(newUser);
                progressDialog.dismiss();
            }
        });
    }
}
