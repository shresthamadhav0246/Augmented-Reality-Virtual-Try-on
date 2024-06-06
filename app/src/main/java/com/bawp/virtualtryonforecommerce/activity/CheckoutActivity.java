package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bawp.virtualtryonforecommerce.R;

public class CheckoutActivity extends AppCompatActivity {

    private TextView textViewProductName;
    private TextView textViewQuantity;
    private TextView textViewTotalPrice;
    private Button btnDecrease;
    private Button btnIncrease;
    private Button btnCheckout;
    private String title;
    private double price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnCheckout = findViewById(R.id.btnCheckout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the title
        toolbar.setTitle("Checkout");

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("title")) {

            title = intent.getStringExtra("title");
            price = intent.getDoubleExtra("price", 0);
        }
        textViewProductName.setText(title);
        textViewTotalPrice.setText(String.valueOf(price));

        // Set initial quantity
        int initialQuantity = 1;
        textViewQuantity.setText(String.valueOf(initialQuantity));

        // Set click listeners
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
    }

    private void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(textViewQuantity.getText().toString());
        if (currentQuantity > 1) {
            currentQuantity--;
            textViewQuantity.setText(String.valueOf(currentQuantity));
            updateTotalPrice(currentQuantity);
        }
    }

    private void increaseQuantity() {
        int currentQuantity = Integer.parseInt(textViewQuantity.getText().toString());
        currentQuantity++;
        textViewQuantity.setText(String.valueOf(currentQuantity));
        updateTotalPrice(currentQuantity);
    }

    private void updateTotalPrice(int quantity) {
        // Calculate total price based on quantity
        double unitPrice = 10.0; // Example unit price
        double totalPrice = unitPrice * quantity;
        textViewTotalPrice.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    private void checkout() {
        // Implement your checkout logic here
        Toast.makeText(this, "Checkout clicked", Toast.LENGTH_SHORT).show();
    }
}
