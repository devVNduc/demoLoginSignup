package com.example.demologinsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demologinsignup.R;
import com.example.demologinsignup.databinding.ActivityDetailBinding;
import com.example.demologinsignup.databinding.ActivityDetailProductBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitle,detailPrice,detailOrigin,detailDesc, detailQuantity, detailStatus;
    ImageView detailImage;

    String key ="";
    String imageUrl = "";
    FloatingActionButton deleteButton, editButton;
    private ActivityDetailProductBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailPrice = findViewById(R.id.detailPrice);
        detailOrigin = findViewById(R.id.detailOrigin);
        detailDesc = findViewById(R.id.detailDesc);
        detailQuantity = findViewById(R.id.detailQuantity);
        detailStatus = findViewById(R.id.detailStatus);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with(DetailActivity.this).load(bundle.get("Image")).into(detailImage);
            detailTitle.setText(bundle.getString("Title"));
            detailPrice.setText(bundle.getString("Price"));
            detailOrigin.setText(bundle.getString("Origin"));
            detailDesc.setText(bundle.getString("Description"));
            detailQuantity.setText(bundle.getString("Quantity"));
            detailStatus.setText(bundle.getString("Status"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
        }


        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this,"Xóa sản phẩm thành công!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainAdminActivity.class));
                        finish();
                    }
                });

            }
        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, Edit.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Price", detailPrice.getText().toString())
                        .putExtra("Origin", detailOrigin.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Quantity", detailQuantity.getText().toString())
                        .putExtra("Status", detailStatus.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(i);
            }
        });
    }
}