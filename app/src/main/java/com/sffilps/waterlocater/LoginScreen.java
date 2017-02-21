package com.sffilps.waterlocater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    // UI references.
    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInButton;
    private Button backButton;
    private ProgressDialog progressDialog;
    boolean signInBool = false;
    boolean registerBool = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        backButton = (Button) findViewById(R.id.back_button);
        signInButton = (Button) findViewById(R.id.signIn_Button);
        signInEmail = (EditText) findViewById(R.id.login_email);
        signInPassword = (EditText) findViewById(R.id.login_password);

        backButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


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
                            signInBool = true;
                        } else {
                            Toast.makeText(LoginScreen.this, "Could not Sign In... Please try again.",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
        return signInBool;

    }
    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            if (signIn()) {
                Context context = v.getContext();
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            }
        } else if (v == backButton) {
            Context context = v.getContext();
            Intent intent = new Intent(context, SplashScreen.class);
            context.startActivity(intent);
        }
    }

    public void onBackPressed(){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SplashScreen.class);
        context.startActivity(intent);
    }
}

