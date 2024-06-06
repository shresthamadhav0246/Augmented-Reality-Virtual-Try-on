package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bawp.virtualtryonforecommerce.PagerAdapter.ImagePagerAdapter;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    // Declare ViewPager and adapter
    private ViewPager imageViewPager;
    private ImagePagerAdapter imagePagerAdapter;
    private TextView productPriceView, productTitleView, productDetailsView;
    private Button virtualTryOnButton, btnCart, btnBuy;
    private ImageView backButton;
    private ImageView cartIcon;


    // Product details
    private List<String> imageUrls = new ArrayList<>();
    private String productName, productDetails;
    private Double productPrice;
    private String modelUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productTitleView = findViewById(R.id.productTitle);
        productPriceView = findViewById(R.id.productPrice);
        productDetailsView = findViewById(R.id.productDetails);
        virtualTryOnButton = findViewById(R.id.virtualTryOnButton);
        backButton = findViewById(R.id.backButton);
        cartIcon = findViewById(R.id.cartIcon);
        btnCart = findViewById(R.id.btnCart);
        btnBuy = findViewById(R.id.buyButton);

        // Initialize ViewPager and adapter
        imageViewPager = findViewById(R.id.imageViewPager);
        imagePagerAdapter = new ImagePagerAdapter(this, imageUrls);
        imageViewPager.setAdapter(imagePagerAdapter);

        // Set onClickListeners for arrow buttons
        ImageButton leftArrow = findViewById(R.id.leftArrow);
        ImageButton rightArrow = findViewById(R.id.rightArrow);
        leftArrow.setOnClickListener(v -> showPreviousImage());
        rightArrow.setOnClickListener(v -> showNextImage());

        // Retrieve the clicked product from the intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("clickedProduct")) {
            Product clickedProduct = intent.getParcelableExtra("clickedProduct");
            if (clickedProduct != null) {
                productName = clickedProduct.getProductName();
                productDetails = clickedProduct.getProductDescription();
                productPrice = clickedProduct.getProductPrice();
                imageUrls = clickedProduct.getProductImages();
                modelUri = clickedProduct.getProductModel();
                updateUI();
            }
        }

        virtualTryOnButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, FurnitureVirtualTryOn.class);
            intent1.putExtra("modelUri",modelUri);
            intent1.putExtra("productDescription",productDetails);
            intent1.putExtra("title", productName);
            intent1.putExtra("price", productPrice);
            startActivity(intent1);
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCart();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckout();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCheckout();
            }
        });
    }
    private void openCart() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    private void openCheckout(){
        Intent intent1 = new Intent(this, CheckoutActivity.class);
        intent1.putExtra("title", productName);
        intent1.putExtra("price", productPrice);
        startActivity(intent1);
    }

    // Method to show the previous image in ViewPager
    private void showPreviousImage() {
        int currentItem = imageViewPager.getCurrentItem();
        if (currentItem > 0) {
            imageViewPager.setCurrentItem(currentItem - 1);
        }
    }

    // Method to show the next image in ViewPager
    private void showNextImage() {
        int currentItem = imageViewPager.getCurrentItem();
        if (currentItem < imagePagerAdapter.getCount() - 1) {
            imageViewPager.setCurrentItem(currentItem + 1);
        }
    }

    // Method to update UI with product details
    private void updateUI() {
        // Update UI components with product details

        productTitleView.setText(productName);
        productPriceView.setText(String.valueOf("£" + productPrice));
        productDetailsView.setText(productDetails);
        imagePagerAdapter.setImageUrls(imageUrls);
        imagePagerAdapter.notifyDataSetChanged();
    }
}
