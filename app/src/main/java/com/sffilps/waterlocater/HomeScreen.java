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

/**
 * Created by ckramer on 2/10/17.
 */

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    private Button signoutButton;
    private FirebaseAuth mAuth;
    private TextView email;
    FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);

        signoutButton = (Button) findViewById(R.id.signout_button);
        email = (TextView) findViewById(R.id.email_text);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        signoutButton.setOnClickListener(this);

        if (currentUser != null) {
            String emailAdd = currentUser.getEmail();
            email.setText("Welcome, " + emailAdd);
        } else {
            email.setText("Didn't work");
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
}
