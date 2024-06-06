package com.bawp.virtualtryonforecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bawp.virtualtryonforecommerce.ClickListener.OnProductClickListener;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.ProductAdapter;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnProductClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private ImageView notificationIcon, category, home, userProfile;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setNestedScrollingEnabled(false);

        notificationIcon = findViewById(R.id.notificationIcon);
        category = findViewById(R.id.category);
        searchEditText = findViewById(R.id.searchEditText);
        home = findViewById(R.id.homeIcon);
        userProfile = findViewById(R.id.userProfileIcon);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList, this);
        recyclerView.setAdapter(adapter);

        FirebaseInitializer.initialize(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Product product = documentSnapshot.toObject(Product.class);
                            addProductView(product);
                            productList.add(product);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Error retrieving products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        category.setOnClickListener(view -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
        });
        home.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
        userProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });

        searchEditText.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_END = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (searchEditText.getRight() - searchEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
//                        performSearch(((EditText) view).getText().toString().trim());
                        Log.d("TAG", "onTouch: "+ "Search function executed");
                        return true;
                    }
                }

                return false;
            }
        });

    }





    private void addProductView(Product product) {
        // Inflate product view layout
        RelativeLayout productLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.product_view, null);

        // Find views within the product layout
        ImageView productImage = productLayout.findViewById(R.id.productImage);
        TextView productName = productLayout.findViewById(R.id.productName);

        // Set product details to the views
        // Example: productImage.setImageURI(product.getImageUri());
        productName.setText("£" + String.valueOf(product.getProductPrice()));
        // Load and display the first image if available
        if (product.getProductImages() != null && product.getProductImages().size() > 0) {
            String firstImageUrl = product.getProductImages().get(0); // Get the URL of the first image
            // Load image into ImageView using your preferred image loading library (e.g., Picasso, Glide)
            // Example using Glide:
            Glide.with(this).load(firstImageUrl).into(productImage);
        } else {
            // If no image is available, you can set a placeholder or hide the ImageView
            productImage.setVisibility(View.GONE);
        }

        productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, 'product' is the clicked product
                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                intent.putExtra("clickedProduct", product); // 'product' is now Parcelable, so it can be passed directly
                startActivity(intent);
            }
        });


        // Get the LinearLayout container inside HorizontalScrollView
        LinearLayout productContainer = findViewById(R.id.productContainer);

        // Add product view to the container
        productContainer.addView(productLayout);
    }

//    private void performSearch(String query) {
//        if (query.isEmpty()) {
//            // Optionally reset the view if search query is cleared
//            adapter.updateProductList(productList); // Assume you have a method to update the list in your adapter
//            return;
//        }
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("products")
//                .whereGreaterThanOrEqualTo("name", query) // Assuming 'name' is the field you want to search
//                .whereLessThanOrEqualTo("name", query + "\uf8ff")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<Product> filteredProducts = new ArrayList<>();
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        Product product = documentSnapshot.toObject(Product.class);
//                        filteredProducts.add(product);
//                    }
//                    adapter.updateProductList(filteredProducts); // Update the recyclerView with filtered results
//                })
//                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Error while searching products", Toast.LENGTH_SHORT).show());
//    }


    @Override
    public void onProductClick(int position) {
        // Get the product at the clicked position
        Product clickedProduct = productList.get(position);

        // Create an Intent to start the ProductDetailsActivity
        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

        // Pass the clicked product as an extra to the intent
        intent.putExtra("clickedProduct", clickedProduct);

        // Start the activity
        startActivity(intent);
    }

}