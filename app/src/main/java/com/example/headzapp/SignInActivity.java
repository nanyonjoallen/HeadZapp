package com.example.headzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText pEmail, pPassword;
    Button signinBtn;
    TextView pSignup, forgotPassword;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
            //connect to xml resources
        pEmail = findViewById(R.id.TextEmailAddress);
        pPassword = findViewById(R.id.enter_password);
        signinBtn = findViewById(R.id.signin_btn);
        pSignup= findViewById(R.id.goto_signup);
        progressBar= findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();//database instance

        //validate user input
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convert data to string
                String email = pEmail.getText().toString().trim();
                String password = pPassword.getText().toString().trim();

                //validate data
                if (TextUtils.isEmpty(email)){
                    pEmail.setError("Emailvis required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    pPassword.setError("Password is required");
                    return;
                }
                if (password.length() <5 ){
                    pPassword.setError("Password must be >=5 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if login is successful or not
                        if (task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT) .show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(SignInActivity.this, "Error!" + task.getException(). getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        pSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

    }
}