package com.sffilps.waterlocater.controllers;

/**
 * Created by ckramer on 2/11/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.Person;

import java.util.ArrayList;
import java.util.List;


public class RegisterScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // UI references.
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerName;
    private Button backButton;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private String username;
    private String role;
    private boolean registerBool = false;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.register_button);
        backButton = (Button) findViewById(R.id.back_button);
        registerEmail = (EditText) findViewById(R.id.register_email);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerName = (EditText) findViewById(R.id.register_name);
        spinner = (Spinner) findViewById(R.id.role_spinner);

        spinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        List<String> roles = new ArrayList<>();
        roles.add("Administrator");
        roles.add("Manager");
        roles.add("User (Default)");
        roles.add("Worker");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(2);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SplashScreen.class);
                context.startActivity(intent);
            }
        });

    }

    /**
     * A method to attempt to register user
     * @return boolean if successful
     */
    private boolean registerUser() {
        final String email = registerEmail.getText().toString().trim();
        final String password = registerPassword.getText().toString().trim();
        final String name = registerName.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email address.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        //creates the user and directs to correct homepage
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerBool = true;
                            String userID = mAuth.getCurrentUser().getUid();
                            Person newUser = new Person(name, email, role, userID, "");
                            DatabaseReference currentUserDB = mDatabase.child(userID);
                            currentUserDB.setValue(newUser);
                            progressDialog.dismiss();
                            Intent i = new Intent(RegisterScreen.this,HomeScreen.class);
                            Intent i2 = new Intent(RegisterScreen.this,HomeScreenWorker.class);
                            if (role == "Administrator" || role == "Manager" || role == "Worker") {
                                startActivity(i2);
                            } else {
                                startActivity(i);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterScreen.this, "Could not register... Please try again.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        return registerBool;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        role = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        role = "User";
    }

    /**
     * makes back button go to splash screeen
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SplashScreen.class);
        context.startActivity(intent);
    }
}
