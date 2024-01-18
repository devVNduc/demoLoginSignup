package com.example.demologinsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class resetPass extends AppCompatActivity {
    private EditText passwordReset,usernameRe;
    private Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        usernameRe =(EditText) findViewById(R.id.usernameRe);
        passwordReset=(EditText) findViewById(R.id.passwordReset);
        btnReset=(Button) findViewById(R.id.btnReset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameReset = usernameRe.getText().toString().trim();
                String passReset = passwordReset.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Lấy đối tượng UserDao từ UserDatabase
                        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                        UserDao userDao = userDatabase.userDao();
                        // Kiểm tra xem người dùng có tồn tại trong cơ sở dữ liệu không
                        User existingUser = userDao.getUserByUsername(usernameReset);
                        if (existingUser != null) {
                            // Cập nhật mật khẩu của người dùng
                            existingUser.setPassword(passReset);
                            userDao.updateUser(existingUser);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(resetPass.this,"Reset success!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(resetPass.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(resetPass.this,"Reset Unsuccess!!!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
            }
        });



    }
}