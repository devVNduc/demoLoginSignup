package com.example.demologinsignup.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demologinsignup.Adapter.MyAdapter;
import com.example.demologinsignup.Adapter.MyAdapterCategory;
import com.example.demologinsignup.Data.DataClass;
import com.example.demologinsignup.Data.DataclassCategory;
import com.example.demologinsignup.R;
import com.example.demologinsignup.databinding.ActivityMainBinding;
import com.example.demologinsignup.databinding.ActivityMainadminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdminActivity extends AppCompatActivity {
    private ActivityMainadminBinding binding;
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    RecyclerView recyclerView, recyclerViewCategory;

    ArrayList<DataClass> dataList;
    ArrayList<DataclassCategory> dataclassCategories;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    SearchView searchView;
    MyAdapter adapter;
    MyAdapterCategory myAdapterCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainadmin);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        searchView = findViewById(R.id.search);

//        GridLayoutManager gridLayoutManagerCategory = new GridLayoutManager(MainAdminActivity.this, 3);
//        recyclerViewCategory.setLayoutManager(gridLayoutManagerCategory);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dataclassCategories = new ArrayList<>();
        myAdapterCategory = new MyAdapterCategory(MainAdminActivity.this, dataclassCategories);
        recyclerViewCategory.setAdapter(myAdapterCategory);

        databaseReference = FirebaseDatabase.getInstance().getReference("Category");

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataclassCategories.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataclassCategory dataclassCategory = itemSnapshot.getValue(DataclassCategory.class);
                    dataclassCategories.add(dataclassCategory);
                }
                myAdapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainAdminActivity.this, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
        builder.setCancelable(false);
//        builder.setView(R.layout.animation);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        adapter = new MyAdapter(MainAdminActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemReselectedListener(item -> {

            if (item.getItemId() == R.id.menu_home) {
                Intent i = new Intent(MainAdminActivity.this, MainAdminActivity.class);
                startActivity(i);
            } else if (item.getItemId() == R.id.menu_search) {
                Intent i = new Intent(MainAdminActivity.this, MainAdminActivity.class);
                startActivity(i);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchData(searchList);
//        recyclerViewCategory.setVisibility(View.GONE);
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        LinearLayout layoutadd = dialog.findViewById(R.id.layoutadd);
        LinearLayout layoutCategory = dialog.findViewById(R.id.layoutCategory);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);

        layoutadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent i = new Intent(MainAdminActivity.this, upload.class);
                startActivity(i);

            }
        });

        layoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(MainAdminActivity.this, CategoryActivity.class);
                startActivity(i);
            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainAdminActivity.this, "", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            Intent i = new Intent(MainAdminActivity.this, MainAdminActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.update) {
            Intent i = new Intent(MainAdminActivity.this, upload.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.updateCategory) {
            Intent i = new Intent(MainAdminActivity.this, CategoryActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.nav_logout) {
            Intent i = new Intent(MainAdminActivity.this, MainAdminActivity.class);
            startActivity(i);
        }
        return false;
    }
}
