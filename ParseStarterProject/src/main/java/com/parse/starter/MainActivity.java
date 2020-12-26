/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.v;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signupModeActive = true;
    TextView loginTextView;
    EditText username;
    EditText pass;
    ImageView logoImageView;
    RelativeLayout layout;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), userActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {   //if we click on "ENTER" or click on the "TICK" symbol then it should mean that we have hit the signup/login button
            signup(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.loginTextView) {
            Button signupButton = findViewById(R.id.signupButton);
            if (signupModeActive) {
                //this means we should toggle to login mode
                signupModeActive = false;
                signupButton.setText("Login");
                loginTextView.setText("Or,Sign Up");
            } else {
                signupModeActive = true;
                signupButton.setText("Sign Up");
                loginTextView.setText("Or,Login");
            }
        } else if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }

    public void signup(View view) {


        if (username.getText().toString().matches("") || pass.getText().toString().matches("")) {
            Toast.makeText(this, "Usrname and pass required", Toast.LENGTH_SHORT).show();
        } else {
            if (signupModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(pass.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showUserList();
                            Toast.makeText(MainActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                //login time baby...kaboooommmmmmmmmm
                ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            showUserList();
                            Log.i("Login", "ok");
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Intagram");

        username = findViewById(R.id.usernameEditText);
        pass = findViewById(R.id.passEditText);
        loginTextView = findViewById(R.id.loginTextView);
        logoImageView = findViewById(R.id.logoImageView);
        layout = findViewById(R.id.backgroundLayout);

        loginTextView.setOnClickListener(this);  //for clicking on logintextview
        pass.setOnKeyListener(this);

        logoImageView.setOnClickListener(this); //these two are for k je aapa screen te click kriye other than userame and password then keyboard gayab hoje te username and pass aale
        layout.setOnClickListener(this);         //edittext to bina do heee cheejan ne hor screen te i.e. logo and khaali background layout...so ohna dona te onclicklistener laado

        if (ParseUser.getCurrentUser() != null) {
            showUserList();   //user is already logged in
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


}