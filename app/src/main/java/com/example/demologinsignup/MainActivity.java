package com.example.demologinsignup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin,btnSignup,btnLoginDone;
    private EditText editUsername,editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        btnLoginDone=(Button)findViewById(R.id.btnLoginDone);
        editUsername=(EditText)findViewById(R.id.editUsername);
        editPassword=(EditText)findViewById(R.id.editPassword);
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
        toast.setGravity(Gravity.CENTER, 0, 1000);

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