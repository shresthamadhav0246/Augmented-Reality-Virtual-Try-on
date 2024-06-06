package com.bawp.virtualtryonforecommerce.activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.ImagesAdapter;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements ImagesAdapter.OnImageRemovedListener {

    private RecyclerView imagesRecyclerView;
    private Spinner spinnerProductModels;
    private EditText productTitle, productDescription, productPrice, productSKU;
    private Button addProductButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    int stockQuantity = 10;
    private List<String> productImageUrls = new ArrayList<>();
    private List<Uri> imageUris = new ArrayList<>();
    private ImagesAdapter imagesAdapter;
    private StorageReference storageReference;

    private Spinner spinnerCategory;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> categories = new ArrayList<>();


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        FirebaseInitializer.initialize(this);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        spinnerProductModels = findViewById(R.id.spinnerProductModels);
        productTitle = findViewById(R.id.product_title);
        productDescription = findViewById(R.id.description);
        productPrice = findViewById(R.id.price);
        addProductButton = findViewById(R.id.add_product_button);
        imageView = findViewById(R.id.product_img); // Replace with your ImageView ID

        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesAdapter = new ImagesAdapter(this, imageUris, this);
        imagesRecyclerView.setAdapter(imagesAdapter);
        FrameLayout frameLayout = findViewById(R.id.framelayout);

        // Initialize views
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Initialize spinner adapter
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

//        Replace with your FrameLayout ID

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

//        fetchModelNamesFromFirebaseStorage();
        fetchModelDataFromFirebaseStorage();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, implement your logic to add product details to Firestore or your preferred database
                // Example:
                String title = productTitle.getText().toString();
                String description = productDescription.getText().toString();
                int price = Integer.parseInt(productPrice.getText().toString());

                String selectedModel = storageReference.toString();

                // Get the selected category name from the spinner
                String selectedCategoryName = spinnerCategory.getSelectedItem().toString();


                Product product = new Product(title, description, price, selectedCategoryName, productImageUrls, selectedModel, stockQuantity);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("products").add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductActivity.this, "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // Save these details along with 'selectedModel' to Firestore
                Toast.makeText(AddProductActivity.this, "Product added!", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch categories from Firestore
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear existing categories
                            categories.clear();

                            // Add categories from Firestore
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryId = document.getId(); // Get the document ID
                                String categoryName = document.getString("name"); // Assuming you have a field called "name" for category name
                                categories.add(categoryName);
                            }

                            // Notify the adapter that the dataset has changed
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting categories: ", task.getException());
                        }
                    }
                });
    }



    private void fetchModelDataFromFirebaseStorage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("models");
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                List<String> modelNames = new ArrayList<>();
                final List<StorageReference> modelReferences = new ArrayList<>(); // Store model references here
                for (StorageReference item : listResult.getItems()) {
                    modelNames.add(item.getName());
                    modelReferences.add(item); // Add corresponding StorageReference to the list
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this,
                        android.R.layout.simple_spinner_item, modelNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductModels.setAdapter(adapter);

                // Set up spinner on item selected listener to get selected model reference
                spinnerProductModels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Get selected model reference
                        storageReference = modelReferences.get(position);
                        // Now you can use this selectedModelReference when saving the product
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle nothing selected if needed
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Failed to fetch models.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                // Handle multiple images
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    uploadFileToFirebaseStorage(imageUri);

                }
            } else if (data.getData() != null) {
                // Handle single image
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                uploadFileToFirebaseStorage(imageUri);
            }
            if (!imageUris.isEmpty()) {
                imagesRecyclerView.setVisibility(View.VISIBLE); // Make the RecyclerView visible
                imagesAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
            }
        }
    }

    private void uploadFileToFirebaseStorage(Uri imageUri) {
        StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads")
                .child(System.currentTimeMillis() + ".jpg"); // Using JPEG as an example

        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        productImageUrls.add(uri.toString());
                        // Check if all images are uploaded then you can proceed to save the product
                        if (allImagesUploaded()) {
                            saveProductDetails();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Assuming you have a method to check if all selected images have been uploaded
    private boolean allImagesUploaded() {
        // Implement logic to check if all selected images have been uploaded
        // For example, compare the size of productImageUrls with the number of selected images
        return true; // Placeholder return value
    }

    private void saveProductDetails() {
        // Similar to your existing logic for saving the product
        // Make sure this is called after all images have been uploaded and their URLs collected
    }


    @Override
    public void onImageRemoved(Uri uri) {
        Toast.makeText(this, "Image removed", Toast.LENGTH_SHORT).show();
    }

}
