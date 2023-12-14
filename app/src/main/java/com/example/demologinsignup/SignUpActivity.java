package com.example.demologinsignup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
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

public class SignUpActivity extends AppCompatActivity {
    private Button btnLogin,btnSignup,btnSignupDone;
    private EditText editTextUsername,editTextPassword,editTextRePassword,editTextName,editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Ánh xạ id
        btnSignupDone=(Button)findViewById(R.id.btnSignupDone);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        editTextUsername=(EditText)findViewById(R.id.editTextUsername);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextRePassword=(EditText)findViewById(R.id.editTextRePassword);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextPhone=(EditText)findViewById(R.id.editTextPhone);

        // Xử lý chuyển trang
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        // Xử lý nút login done
        btnSignupDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SignupUsername = editTextUsername.getText().toString().trim();
                if (isValidUsername(SignupUsername)) {
                    // User hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                } else {
                    // Email không hợp lệ
                    showToast("Username không hợp lệ",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editTextUsername.requestFocus();
                    editTextUsername.selectAll();
                    return;
                }
                String SignupPhone = editTextPhone.getText().toString().trim();
                if (isValidPhoneNumber(SignupPhone)){
                    // Số điện thoại hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                }
                else{
                    showToast("Số điện thoại không hợp lệ",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editTextPhone.requestFocus();
                    editTextPhone.selectAll();
                    return;
                }
                String SignupPassword = editTextPassword.getText().toString().trim();
                if (isValidPassword(SignupPassword)) {
                    // Mật khẩu hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                } else {
                    // Mật khẩu không hợp lệ
                    showToast("Password không hợp lệ",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editTextPassword.requestFocus();
                    editTextPassword.selectAll();
                    return;
                }
                String SignupRePassword = editTextRePassword.getText().toString().trim();
                if(SignupPassword.equals(SignupRePassword)) {
                    // Mật khẩu trùng khớp
                    // Tiếp tục xử lý các bước đăng ký khác
                }
                else {
                    // Mật khẩu không trùng khớp
                    showToast("Mật khẩu không trùng khớp",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editTextRePassword.requestFocus();
                    editTextRePassword.selectAll();
                    return;
                }
                String SignupName = editTextName.getText().toString().trim();
                if (isValidFullName(SignupName)){
                    // Họ tên người hợp lệ
                    // Tiếp tục xử lý các bước đăng ký khác
                }
                else{
                    showToast("Họ tên người không hợp lệ",R.layout.custom_toastfail_layout,R.drawable.toastfail_background);
                    editTextName.requestFocus();
                    editTextName.selectAll();
                    return;
                }

                String TongHop = "Name: " + SignupName + "\n" + "Phone: " + SignupPhone + "\n"+ "Username: " + SignupUsername;
                showToast("Đăng ký thành công",R.layout.custom_toastsuccess_layout,R.drawable.toastsuccess_background);
                AlertDialog.Builder mydialog = new AlertDialog.Builder(SignUpActivity.this);
                mydialog.setTitle("Thông tin đăng ký");
                mydialog.setMessage(TongHop);
                mydialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mydialog.create().show();
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
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy để kiểm tra số điện thoại
        String phoneRegex = "^[+]?[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(phoneRegex);

        // Kiểm tra khớp với biểu thức chính quy
        return pattern.matcher(phoneNumber).matches();
    }
    public boolean isValidFullName(String fullName) {
        // Biểu thức chính quy để kiểm tra họ và tên
        String nameRegex = "^[a-zA-Z\\s]+";
        Pattern pattern = Pattern.compile(nameRegex);

        // Kiểm tra khớp với biểu thức chính quy
        return pattern.matcher(fullName).matches();
    }
    public void showToast(String message,int layoutResId, int drawableResId) {
        // Tạo Toast
        Toast toast = new Toast(getApplicationContext());

        // Thiết lập thời gian hiển thị và vị trí của Toast
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 1200);

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