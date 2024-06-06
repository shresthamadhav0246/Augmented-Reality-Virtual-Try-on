package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bawp.virtualtryonforecommerce.R;

public class UserProfileActivity extends AppCompatActivity {

    private TextView settingOption;
    private TextView productsOption;
    private TextView manageProfileOption;
    private TextView logoutOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        settingOption = findViewById(R.id.setting_option);
        productsOption = findViewById(R.id.products_option);
        manageProfileOption = findViewById(R.id.manage_profile_option);
        logoutOption = findViewById(R.id.logout_option);

        // Setup click listeners
        settingOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Settings click
                openSettings();
            }
        });

        productsOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Products click
                viewProducts();
            }
        });



        manageProfileOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Manage Profile click
                manageProfile();
            }
        });

        logoutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Logout
                logout();
            }
        });
    }

    private void openSettings() {
        // Navigate to Settings Activity or Fragment
    }

    private void viewProducts() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


    private void manageProfile() {
        // Navigate to Manage Profile Activity or Fragment
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
