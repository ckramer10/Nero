package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.controllers.LoginScreen;
import com.sffilps.waterlocater.controllers.RegisterScreen;

/**
 * Created by ckramer on 2/11/17.
 */

public class SplashScreen extends AppCompatActivity {

    private Button signInButton;
    private Button registerButton;
    private TextView title;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        title = (TextView) findViewById(R.id.title);
        registerButton = (Button) findViewById(R.id.register_button);
        signInButton = (Button) findViewById(R.id.login_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LoginScreen.class);
                context.startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RegisterScreen.class);
                context.startActivity(intent);
            }
        });

        title.startAnimation(animation);

    }

    /**
     * Sets the back button to exit the app
     */
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
