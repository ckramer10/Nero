package com.sffilps.waterlocater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ckramer on 2/11/17.
 */

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        registerButton = (Button) findViewById(R.id.register_button);
        signInButton = (Button) findViewById(R.id.login_button);

        registerButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            Context context = v.getContext();
            Intent intent = new Intent(context, LoginScreen.class);
            context.startActivity(intent);

        }

        if (v == registerButton) {
            Context context = v.getContext();
            Intent intent = new Intent(context, RegisterScreen.class);
            context.startActivity(intent);
        }
    }
}