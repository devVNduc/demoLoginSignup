package com.example.demologinsignup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.HashMap;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin,btnSignup,btnLoginDone,btnLoginWithGoogle;
    private EditText editUsername,editPassword;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private TextView textForgotPassword;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textForgotPassword=(TextView)findViewById(R.id.textForgotPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        btnLoginDone=(Button)findViewById(R.id.btnLoginDone);
        editUsername=(EditText)findViewById(R.id.editUsername);
        editPassword=(EditText)findViewById(R.id.editPassword);
        btnLoginWithGoogle=(Button)findViewById(R.id.btnLoginWithGoogle);
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false)
                .build();
        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    String username = credential.getId();
                    String password = credential.getPassword();
                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d("TAG", "Got ID token.");
                        Intent intent = new Intent(MainActivity.this,HomeScreenUser.class);
                        startActivity(intent);

                    } else if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d("TAG", "Got password.");
                        showToast("Error Pass",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                    Log.d("TAG", String.valueOf(e));
                    showToast("Error Sign",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                }
            }
        });
        btnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No saved credentials found. Launch the One Tap sign-up flow, or
                                // do nothing and continue presenting the signed-out UI.
                                Log.d("TAG GG", e.getLocalizedMessage());
                            }
                        });
            }
        });
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendOTPActivity.class);
                startActivity(intent);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLoginDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LoginUsername = editUsername.getText().toString().trim();
                String LoginPassword = editPassword.getText().toString().trim();
                if(isValidUsername(LoginUsername)){
                    // User hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                }else{
                    // User không hợp lệ
                    showToast("Username không hợp lệ",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editUsername.requestFocus();
                    editUsername.selectAll();
                    return;
                }

                if (isValidPassword(LoginPassword)) {
                    // Mật khẩu hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                } else {
                    // Mật khẩu không hợp lệ
                    showToast("Password không hợp lệ", R.layout.custom_toastfail_layout, R.drawable.toastfail_background);
                    editPassword.requestFocus();
                    editPassword.selectAll();
                    return;
                }
                // Perform query
                UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                UserDao userDao = userDatabase.userDao();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = userDao.login(LoginUsername,LoginPassword);
                        if(user == null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("Invalid Credentials!!!", R.layout.custom_toastfail_layout, R.drawable.toastfail_background);
                                }
                            });
                        }else{
                            if (LoginUsername.equals("admin") && LoginPassword.equals("Admin123!")){
                                startActivity(new Intent(MainActivity.this,HomeScreen.class));
                            }else{
                                startActivity(new Intent(MainActivity.this, HomeScreenUser.class));
                            }

                        }
                    }
                }).start();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }
    public boolean isValidUsername(String email) {
        // Biểu thức chính quy để kiểm tra định dạng username
        String usernameRegex = "^[a-zA-Z][a-zA-Z0-9_]{2,15}$";
        Pattern pattern = Pattern.compile(usernameRegex);

        // Kiểm tra khớp với biểu thức chính quy
        return pattern.matcher(email).matches();
    }
    public boolean isValidPassword(String password) {
        // Kiểm tra độ dài mật khẩu (ít nhất 8 ký tự)
        if (password.length() < 8) {
            return false;
        }

        // Kiểm tra sự hiện diện của chữ hoa và chữ thường
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
        }
        if (!hasUpperCase || !hasLowerCase) {
            return false;
        }

        // Kiểm tra sự hiện diện của chữ số
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        if (!hasDigit) {
            return false;
        }

        // Kiểm tra sự hiện diện của ký tự đặc biệt
        boolean hasSpecialChar = false;
        String specialChars = "!@#$%^&*()-_=+[{]};:'\",<.>/?";
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (specialChars.contains(String.valueOf(c))) {
                hasSpecialChar = true;
                break;
            }
        }
        if (!hasSpecialChar) {
            return false;
        }

        // Mật khẩu hợp lệ
        return true;
    }
    public void showToast(String message,int layoutResId, int drawableResId) {
        // Tạo Toast
        Toast toast = new Toast(getApplicationContext());

        // Thiết lập thời gian hiển thị và vị trí của Toast
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -1100);

        // Tạo đối tượng LayoutInflater để inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(layoutResId, findViewById(R.id.custom_toast_container));

        // Thiết lập layout cho Toast
        toast.setView(layout);

        // Thiết lập văn bản của Toast
        TextView textView = layout.findViewById(R.id.toast_text);
        textView.setText(message);

        // Thiết lập hình dạng bo góc cho Toast
        Drawable background = ContextCompat.getDrawable(getApplicationContext(), drawableResId);
        layout.setBackground(background);

        // Hiển thị Toast
        toast.show();
    }
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có muốn thoát khỏi ứng dụng không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Thực hiện hành động thoát ứng dụng ở đây
                finish();
            }
        });
        builder.setNegativeButton("Không", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}