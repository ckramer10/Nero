package com.sffilps.waterlocater.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sffilps.waterlocater.R;


public class LoginScreen extends AppCompatActivity  {

    // UI references.
    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInButton;
    private Button backButton;
    private ProgressDialog progressDialog;
    boolean signInBool = false;
    boolean registerBool = false;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        backButton = (Button) findViewById(R.id.back_button);
        signInButton = (Button) findViewById(R.id.signIn_Button);
        signInEmail = (EditText) findViewById(R.id.login_email);
        signInPassword = (EditText) findViewById(R.id.login_password);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    /**
     * Method to Sign-In User
     * @return boolean if successful
     */
    private boolean signIn() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email address.",Toast.LENGTH_SHORT).show();
            return signInBool;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.",Toast.LENGTH_SHORT).show();
            return signInBool;
        }

        progressDialog.setMessage("Signing In User...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                            final String uID = currentUser.getUid();
                            mDatabase.child(uID).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value
                                            role = ((String) dataSnapshot.child("role").getValue());
                                            Intent i = new Intent(LoginScreen.this,HomeScreen.class);
                                            Intent i2 = new Intent(LoginScreen.this,HomeScreenWorker.class);

                                            if (role.equals("Administrator") || role.equals("Manager") || role.equals("Worker")) {
                                                startActivity(i2);
                                            } else {
                                                startActivity(i);
                                            }
                                            
                                            signInBool = true;
                                            progressDialog.dismiss();


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginScreen.this, "Could not Sign In... Please try again.",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        return signInBool;

    }

    /**
     * method making back button go to splash screen
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SplashScreen.class);
        context.startActivity(intent);
    }
}

