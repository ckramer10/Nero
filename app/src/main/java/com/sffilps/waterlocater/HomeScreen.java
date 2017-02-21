package com.sffilps.waterlocater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ckramer on 2/10/17.
 */

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private Button signoutButton;
    private FirebaseAuth mAuth;
    private TextView name;
    FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        signoutButton = (Button) findViewById(R.id.signout_button);
        name = (TextView) findViewById(R.id.email_text);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        signoutButton.setOnClickListener(this);

        if (currentUser != null) {
            String email = currentUser.getEmail();
            final String uID = currentUser.getUid();
            mDatabase.child(uID).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            setUserName((String)dataSnapshot.child("name").getValue());
                            name.setText("Name: " + userName);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            name.setText("Didn't work");
        }
    }



    @Override
    public void onClick(View v) {
        if (v == signoutButton) {
            mAuth.getInstance().signOut();
            Context context = v.getContext();
            Intent intent = new Intent(context, SplashScreen.class);
            context.startActivity(intent);
            Toast.makeText(HomeScreen.this, "Signed Out.",Toast.LENGTH_LONG).show();
        }
    }

    public void setUserName(String s) {
        userName = s;
    }
}
