package com.example.demologinsignup;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;


public class HomeScreen extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigationview_trangchu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.mo, R.string.dong) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.logoutMenuItem) {
            // Gọi phương thức đăng xuất khi menu "Đăng xuất" được chọn
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkLoginStatus() {
        return false;
    }

    private void logout() {

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = checkLoginStatus();

        if (!isLoggedIn) {
            // Nếu chưa đăng nhập, chuyển đến màn hình đăng nhập và kết thúc MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        // Chuyển đến màn hình đăng nhập hoặc màn hình khác tùy thuộc vào logic của ứng dụng
        resetAppState();
        // Đóng màn hình hiện tại (MainActivity) đaể ngăn người dùng trở lại nó bằng cách nhấn nút "Back"
        finish();
    }

    private void resetAppState() {
    }
}

