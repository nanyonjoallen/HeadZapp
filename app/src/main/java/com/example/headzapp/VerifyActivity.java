package com.example.headzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyActivity extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4;
    private TextView resendBtn;

    //true after 60 seconds
    private boolean resendEnable = false;

    //resend in seconds
    private int resendTime =60;

    private int selectedOTPPosition =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otp1 = findViewById(R.id.OTP1);
        otp2 = findViewById(R.id.OTP2);
        otp3 = findViewById(R.id.OTP3);
        otp4 = findViewById(R.id.OTP4);
        resendBtn = findViewById(R.id.resendOTP);

        final Button verifyBtn = findViewById(R.id.verify_btn);
        final TextView otpNumber= findViewById(R.id.OTP_number);

        // get phone number from signUp activity through intent
         final String getOtpNumber = getIntent().getStringExtra("phoneNumber");

         //set phone number to the text view
        otpNumber.setText(getOtpNumber);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);

        //default open keyboard at opt1
        showKeyboard(otp1);

        //start resend count down timer
        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnable){
                    // handle resend code

                    //start new resend count down timer
                    startCountDownTimer();
                }

            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String generateOtp = otp1.getText().toString()+ otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();
                if (generateOtp.length() ==4){

                    //handle your otp verification here
                }
            }
        });

    }

    EditText otpEntry;{
        otpEntry.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpEntry, InputMethodManager.SHOW_IMPLICIT );

    }

    private void startCountDownTimer(){

        resendEnable = false;
        resendBtn.setTextColor(Color.parseColor("#f9c332"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUtilFinished) {
                resendBtn.setText("Resend Code ( " +(millisUtilFinished / 1000)+")");

            }

            @Override
            public void onFinish() {
                resendEnable = true;
                resendBtn.setText("Resend Code");
                resendBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        };{

        }start();

    }

    private void start() {
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0){
                if (selectedOTPPosition == 0){
                    //
                    selectedOTPPosition =1;
                    showKeyboard(otp2);
                }
                else if (selectedOTPPosition ==1){
                    //
                    selectedOTPPosition =2;
                    showKeyboard(otp3);
                }
                else if (selectedOTPPosition ==2){
                    //
                    selectedOTPPosition =3;
                    showKeyboard(otp4);
                }
            }

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_DEL){
            if (selectedOTPPosition ==3){
                selectedOTPPosition =2;
                showKeyboard(otp3);
            }
            else if (selectedOTPPosition ==2){
                selectedOTPPosition =1;
                showKeyboard(otp2);
            }
            else if (selectedOTPPosition ==1){
                selectedOTPPosition=0;
                showKeyboard(otp1);
            }
            return true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }

    }

    private void showKeyboard(EditText otp2) {
    }


    public static class UserHelperClass {

        String fullName, email, phone, password;

        public UserHelperClass() {

        }

        public UserHelperClass(String fullName, String email, String phone, String password) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}