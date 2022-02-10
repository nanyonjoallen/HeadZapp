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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText pFullName, pEmail, pPassword, pPhone, comPassword;
    Button signupBtn;
    TextView pSignin;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
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
        comPassword = findViewById(R.id.confirm_password);

        progressBar= findViewById(R.id.progressBar);

        //initialize firebase
        fAuth = FirebaseAuth.getInstance();//database instance

        //check if user is already loggged in
       //if(fAuth.getCurrentUser() !=null){
          // startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //finish();
       //}

        //
        pSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("Users");
                databaseReference.setValue("First storage");

                //get all the values from the input files
                //convert data to string
                String fullName = pFullName.getText().toString().trim();
                String email = pEmail.getText().toString().trim();
                String phone = pPhone.getText().toString().trim();
                String password = pPassword.getText().toString().trim();
                String cPassword = comPassword.getText().toString().trim();

                UserHelperClass helperClass = new UserHelperClass(fullName, email, phone,password,cPassword);
                databaseReference.child(phone).setValue(helperClass);


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
                            //Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT) .show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

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


