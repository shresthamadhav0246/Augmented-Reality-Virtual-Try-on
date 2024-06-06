package com.bawp.virtualtryonforecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bawp.virtualtryonforecommerce.R;

public class CategoryActivity extends AppCompatActivity {
    private ImageView imageViewSunglasses, imageViewWatch, imageViewFurniture;
    private ImageView notificationIcon, category, home, userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        imageViewSunglasses = findViewById(R.id.imageViewSunglasses);
        imageViewWatch = findViewById(R.id.imageViewCategory2);
        imageViewFurniture = findViewById(R.id.imageViewCategory3);
        home = findViewById(R.id.homeIcon);
        userProfile = findViewById(R.id.userProfileIcon);

        home.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
        userProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });


        imageViewSunglasses.setOnClickListener(view -> {
            Intent intent = new Intent(this, SunglassesCategoryActivity.class);
            startActivity(intent);
        });

        imageViewWatch.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        imageViewFurniture.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });


    }
}