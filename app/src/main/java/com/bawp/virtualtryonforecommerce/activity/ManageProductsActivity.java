package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.ProductAdapter;
import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.ProductManageAdapter;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.bawp.virtualtryonforecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity implements ProductManageAdapter.OnProductListener {

    private DatabaseReference databaseReference;
    private FloatingActionButton addProductFab;
    private ProductManageAdapter productAdapter;
    private List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        RecyclerView productsRecyclerView = findViewById(R.id.productsRecyclerView);
        addProductFab = findViewById(R.id.addProductFab);
        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        productAdapter = new ProductManageAdapter(productList, this);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(productAdapter);

        addProductFab.setOnClickListener(view -> {
            // Intent to add a new product, not directly adding to Firebase here
            // startActivity(new Intent(ManageProductsActivity.this, AddProductActivity.class));
        });

        fetchProducts();
    }

    private void fetchProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageProductsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClicked(int position) {
        // Open EditProductActivity with product details
        // Pass product ID or key to edit
        Product product = productList.get(position);
//        Intent intent = new Intent(ManageProductsActivity.this, EditProductActivity.class);
//        intent.putExtra("productKey", product.getId()); // Assuming 'id' is the key in Firebase
//        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {
        // Delete product from Firebase
//        deleteProduct(productList.get(position));
    }

    private void deleteProduct(String productId) {
        databaseReference.child(productId).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(ManageProductsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ManageProductsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show());
    }
}
