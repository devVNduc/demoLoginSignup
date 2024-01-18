package com.example.demologinsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private EditText usernameOTP,phonenumberOTP;
    private Button btnSenOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpactivity);
        final ProgressBar progressBar = findViewById(R.id.progessBar);
        usernameOTP=(EditText) findViewById(R.id.usernameOTP);
        phonenumberOTP=(EditText) findViewById(R.id.phonenumberOTP);
        btnSenOTP=(Button) findViewById(R.id.btnSenOTP);
        btnSenOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTPusername = usernameOTP.getText().toString().trim();
                String OTPphonenumber = phonenumberOTP.getText().toString().trim();
                UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                UserDao userDao = userDatabase.userDao();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = userDao.getUserByUsernameAndPhoneNumber(OTPusername,OTPphonenumber);
                        if (user!=null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = "User Valid!!!";
                                    Toast.makeText(SendOTPActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                    btnSenOTP.setVisibility(View.INVISIBLE);
                                    Log.d("phone", String.valueOf(OTPphonenumber));
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            "+84" + OTPphonenumber,
                                            60, TimeUnit.SECONDS,
                                            SendOTPActivity.this,
                                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                                @Override
                                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                    progressBar.setVisibility(View.GONE);
                                                    btnSenOTP.setVisibility(View.VISIBLE);
                                                }

                                                @Override
                                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    btnSenOTP.setVisibility(View.VISIBLE);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(SendOTPActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    progressBar.setVisibility(View.GONE);
                                                    btnSenOTP.setVisibility(View.VISIBLE);
                                                    Intent intent = new Intent(getApplicationContext(),VerifyOTPActivity.class);
                                                    intent.putExtra("mobile",String.valueOf(OTPphonenumber));
                                                    intent.putExtra("username",OTPusername);
                                                    intent.putExtra("verificationId",verificationId);
                                                    startActivity(intent);

                                                }
                                            }
                                    );
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = "User Invalid!!!";
                                    Toast.makeText(SendOTPActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }).start();
            }
        });

    }

}