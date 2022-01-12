package com.example.headzapp;

import androidx.activity.result.contract.ActivityResultContracts;
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

public class SignUpActivity extends AppCompatActivity {

    EditText pFullName, pEmail, pPassword;
    Button signupBtn;
    TextView pSignin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pFullName = findViewById(R.id.TextPersonName);
        pEmail = findViewById(R.id.TextEmailAddress2);
        pPassword = findViewById(R.id.create_password);
        signupBtn = findViewById(R.id.signup_btn);
        pSignin= findViewById(R.id.goto_signin);
        progressBar= findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();//database instance

        //check if user is already loggged in
        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convert data to string
                String email = pEmail.getText().toString().trim();
                String password = pPassword.getText().toString().trim();

                //validate data
                if (TextUtils.isEmpty(email)){
                    pEmail.setError("Password is required");
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

                //register user to firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if registration is successful
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT) .show();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(SignUpActivity.this, "Error!" + task.getException(). getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        pSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

    }
}