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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText pEmail, pPassword;
    Button signinBtn;
    TextView pSignup;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    //object to database reference
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //connect to xml resources

        final EditText pEmail = findViewById(R.id.TextEmailAddress);
        final EditText pPassword = findViewById(R.id.enter_password);
        final Button signinBtn = findViewById(R.id.signin_btn);
        final TextView forgotPass = findViewById(R.id.forgot_password);
        final TextView pSignup = findViewById(R.id.goto_signup);

        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();//database instance

        pSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //validate user input
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //convert data to string
                String email = pEmail.getText().toString().trim();
                String password = pPassword.getText().toString().trim();

                //validate data
                if (TextUtils.isEmpty(email)){
                    pEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    pPassword.setError("Password is required");
                    return;
                }
                if (password.length() <5 ){
                    pPassword.setError("Password must be >=5 characters");
                    return;
                }else {
                    isUser();
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

    private void isUser() {
       String userEnteredEmail = pEmail.getText().toString();
        String userEnteredPassword = pPassword.getText().toString();

        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = databaseReference.orderByChild("email").equalTo(userEnteredEmail);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot  snapshot) {
                if (snapshot.exists()){
                    pEmail.setError(null);

                    String passwordFromDB = snapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)){
                        String fullNameFromDB = snapshot.child(userEnteredEmail).child("fullName").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredEmail).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredEmail).child("phone").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                        intent.putExtra ("fullName",fullNameFromDB);
                        intent.putExtra ("email",emailFromDB);
                        intent.putExtra ("phone",phoneFromDB);
                        intent.putExtra ("password",passwordFromDB);

                        startActivity(intent);

                    }
                    else {
                        pPassword.setError("Wrong password");
                        pPassword.requestFocus();

                    }

                }
                else{
                    pEmail.setError("no such password");
                    pEmail.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}



