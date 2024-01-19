package com.example.demologinsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demologinsignup.Animation.animation;
import com.example.demologinsignup.Data.DataClass;
import com.example.demologinsignup.Data.DataclassCategory;
import com.example.demologinsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class upload extends AppCompatActivity {
    ImageView update_Image;
    Button updateButton;
    EditText updateTitle, updatePrice, updateOrigin, updateDesc, updateQuantity, updateStatus;
    Spinner updateCategoryName;
    String imageURL;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        update_Image = findViewById(R.id.updateImage);
        updateButton = findViewById(R.id.updateButton);
        updateTitle = findViewById(R.id.updateTitle);
        updatePrice = findViewById(R.id.updatePrice);
        updateOrigin = findViewById(R.id.updateOrigin);
        updateDesc = findViewById(R.id.updateDesc);
        updateQuantity = findViewById(R.id.updateQuantity);
        updateStatus = findViewById(R.id.updateStatus);
        updateCategoryName = findViewById(R.id.updateCategoryName);



        DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference("Category");
        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataclassCategory> categoryList = new ArrayList<>();
                List<String> categoryNameList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataclassCategory dataclassCategory = dataSnapshot.getValue(DataclassCategory.class);
                    categoryList.add(dataclassCategory);
                    categoryNameList.add(dataclassCategory.getCategoryName());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(upload.this, android.R.layout.simple_spinner_item, categoryNameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                updateCategoryName.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });







        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            update_Image.setImageURI(uri);
                        } else {
                            Toast.makeText(upload.this, "Bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        update_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Products Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(upload.this);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

public void uploadData(){

    String selectedCategoryName = updateCategoryName.getSelectedItem().toString();

    DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference("Category");
    categoryReference.orderByChild("categoryName").equalTo(selectedCategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                DataclassCategory selectedCategory = snapshot.getValue(DataclassCategory.class);

                String categoryId = selectedCategory.getCategoryId();
                String title = updateTitle.getText().toString();
                String price = updatePrice.getText().toString();
                String origin = updateOrigin.getText().toString();
                String desc = updateDesc.getText().toString();
                String quantity = updateQuantity.getText().toString();
                String status = updateStatus.getText().toString();
                DataClass dataClass = new DataClass(title, price, origin, desc, quantity, status, imageURL, categoryId);

                String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                FirebaseDatabase.getInstance().getReference("Products").child(currentDate)
                        .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(upload.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(upload.this, MainAdminActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(upload.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(upload.this, "Error querying Firebase", Toast.LENGTH_SHORT).show();
        }
    });
}

}
