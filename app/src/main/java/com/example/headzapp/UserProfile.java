package com.example.headzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    TextView fullNameLabel,emailLabel,phoneLabel,passwordLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //show all data
        showAllUserData();
    }

    private  void showAllUserData() {
        Intent intent = getIntent();
        String user_email = intent.getStringExtra("email");
        String user_phone = intent.getStringExtra("phone");
        String user_fullName = intent.getStringExtra("fullName");
        String user_password = intent.getStringExtra("password");

    fullNameLabel.setText(user_fullName);
    phoneLabel.setText(user_phone);
    emailLabel.setText(user_fullName);
    passwordLabel.setText(user_password);
    }
}