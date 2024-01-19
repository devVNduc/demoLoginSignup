package com.example.demologinsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.demologinsignup.Data.DataClass;
import com.example.demologinsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Edit extends AppCompatActivity {

    ImageView editImage;
    Button editButton;
    EditText editCategoryId,editTitle, editPrice, editOrigin, editDesc, editQuantity, editStatus ;

    String categoryid, title, price, origin, desc, quantity, status;

    String imageUrl;
    String key, oldImageURL;
    Uri uri;

    DatabaseReference databaseReference;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editCategoryId = findViewById(R.id.editCategoryId);
        editButton= findViewById(R.id.editButton);
        editTitle= findViewById(R.id.editTitle);
        editPrice= findViewById(R.id.editPrice);
        editOrigin= findViewById(R.id.editOrigin);
        editDesc= findViewById(R.id.editDesc);
        editImage= findViewById(R.id.editImage);
        editQuantity = findViewById(R.id.editQuantity);
        editStatus = findViewById(R.id.editStatus);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            editImage.setImageURI(uri);
                        }
                        else {
                            Toast.makeText(Edit.this,"Bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(Edit.this).load(bundle.get("Image")).into(editImage);
            editCategoryId.setText(bundle.getString("CategoryId"));
            editTitle.setText(bundle.getString("Title"));
            editPrice.setText(bundle.getString("Price"));
            editOrigin.setText(bundle.getString("Origin"));
            editDesc.setText(bundle.getString("Description"));
            editQuantity.setText(bundle.getString("Quantity"));
            editStatus.setText(bundle.getString("Status"));
            key = bundle.getString("Key");
            oldImageURL =bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(key);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent i = new Intent(Edit.this, MainAdminActivity.class);
                startActivity(i);
            }
        });
    }
    public  void saveData(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Products Images").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
        builder.setCancelable(false);
//        builder.setView(R.layout.animation);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void updateData(){
        categoryid = editCategoryId.getText().toString().trim();
        title = editTitle.getText().toString().trim();
        price = editPrice.getText().toString().trim();
        origin = editOrigin.getText().toString().trim();
        desc = editDesc.getText().toString().trim();
        quantity = editQuantity.getText().toString().trim();
        status = editStatus.getText().toString().trim();

        DataClass dataClass = new DataClass(title, price, origin, desc, quantity, status, imageUrl,categoryid);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(Edit.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}