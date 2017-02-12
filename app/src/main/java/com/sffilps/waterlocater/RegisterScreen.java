package com.sffilps.waterlocater;

/**
 * Created by ckramer on 2/11/17.
 */

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


public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    // UI references.
    private EditText registerEmail;
    private EditText registerPassword;
    private Button backButton;
    private Button registerButton;
    private ProgressDialog progressDialog;
    boolean signInBool = false;
    boolean registerBool = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.register_button);
        backButton = (Button) findViewById(R.id.back_button);
        registerEmail = (EditText) findViewById(R.id.register_email);
        registerPassword = (EditText) findViewById(R.id.register_password);

        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean registerUser() {
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter a valid email address.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.",Toast.LENGTH_SHORT).show();
            return registerBool;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerBool = true;
                        } else {
                            Toast.makeText(RegisterScreen.this, "Could not register... Please try again.",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
        return registerBool;
    }


    @Override
    public void onClick(View v) {
        if (v == registerButton) {
            if (registerUser()) {
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
}
