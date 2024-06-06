package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.bawp.virtualtryonforecommerce.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCategoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText categoryNameEditText;
    private Button addCategoryButton;
    private Uri imageUri;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        FirebaseInitializer.initialize(this);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("categoryImages");

        imageView = findViewById(R.id.product_img);
        categoryNameEditText = findViewById(R.id.etCategoryName);
        addCategoryButton = findViewById(R.id.add_category_image);
        FrameLayout frameLayout = findViewById(R.id.framelayout);

        frameLayout.setOnClickListener(v -> openFileChooser());

        addCategoryButton.setOnClickListener(v -> {
            uploadFileToFirebaseStorage();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Category Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadFileToFirebaseStorage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String categoryName = categoryNameEditText.getText().toString().trim();
                            String imageUrl = uri.toString();
                            // Here, implement logic to create a new category object and save it to Firestore
                            // For simplicity, only the image URL and category name are used
                            Category category = new Category(categoryName, imageUrl);
                            saveCategoryDetails(category);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCategoryActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCategoryDetails(Category category) {
        // Assuming you have a 'categories' collection in Firestore
        db.collection("categories").add(category)
                .addOnSuccessListener(documentReference -> Toast.makeText(AddCategoryActivity.this, "Category added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddCategoryActivity.this, "Error adding category: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
