package com.example.demologinsignup.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demologinsignup.Adapter.MyAdapter;
import com.example.demologinsignup.Data.DataClass;
import com.example.demologinsignup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Product_Category_Activity extends AppCompatActivity {
    RecyclerView recyclerViewCategory;
    ArrayList<DataClass> dataList;

    DatabaseReference databaseReference;
    SearchView searchcategory;
    MyAdapter adapter;
    ValueEventListener eventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        recyclerViewCategory = findViewById(R.id.recyclerViewProducts);
        searchcategory = findViewById(R.id.searchcategory);
        searchcategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CategoryId")) {
            String categoryId = intent.getStringExtra("CategoryId");

            GridLayoutManager gridLayoutManagerCategory = new GridLayoutManager(Product_Category_Activity.this, 1);
            recyclerViewCategory.setLayoutManager(gridLayoutManagerCategory);

            dataList = new ArrayList<>();
            adapter = new MyAdapter(Product_Category_Activity.this, dataList);
            recyclerViewCategory.setAdapter(adapter);
            databaseReference = FirebaseDatabase.getInstance().getReference("Products");
            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        DataClass dataClass = itemSnapshot.getValue(DataClass.class);

                        if (dataClass != null && categoryId.equals(dataClass.getCategoryId())) {
                            dataList.add(dataClass);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (dataList.isEmpty()) {

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }
    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for(DataClass dataClass: dataList){
            if(dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchData(searchList);
    }
}