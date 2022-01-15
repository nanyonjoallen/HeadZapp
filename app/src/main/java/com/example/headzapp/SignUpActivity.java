package com.example.headzapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    EditText pFullName, pEmail, pPassword, pPhone;
    Button signupBtn;
    TextView pSignin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pFullName = findViewById(R.id.TextPersonName);
        pEmail = findViewById(R.id.TextEmailAddress2);
        pPhone =findViewById(R.id.phoneNumber);
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
                final String fullName = pFullName.getText().toString().trim();
                final String email = pEmail.getText().toString().trim();
                final String phone = pPhone.getText().toString().trim();
                final String password = pPassword.getText().toString().trim();

                //validate data
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)){
                    Toast.makeText(SignUpActivity.this, "All field are required", Toast.LENGTH_SHORT).show();

                }
                if (password.length() <5 ){
                    pPassword.setError("Password must be >=5 characters");
                    return;
                    
                }else{
                    register(fullName,email,phone,password);
                }

            }
        });

    }

    private void register(final String fullName, final String email,  final String phone, final String password) {

        progressBar.setVisibility(View.VISIBLE);
        //register user to firebase
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //check if registration is successful
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT) .show();
                    FirebaseUser ruser = fAuth.getCurrentUser();
                    String userId = ruser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

                    //save the data
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId",userId);
                    hashMap.put("fullName",pFullName.getText().toString());
                    hashMap.put("email", pEmail.getText().toString());
                    hashMap.put("phone", pPhone.getText().toString());
                    hashMap.put("password", pPassword.getText().toString());
                    hashMap.put("imageURL","default");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                            }else{
                                Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()). getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()). getMessage(), Toast.LENGTH_SHORT).show();

                }

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