package com.bawp.virtualtryonforecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bawp.virtualtryonforecommerce.R;

public class AdminActivity extends AppCompatActivity {

    private CardView cardAddProduct, cardManageCategory, cardManageUsers, cardManageOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize CardViews
        cardAddProduct = findViewById(R.id.card_add_product);
        cardManageCategory = findViewById(R.id.card_manage_category);
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardManageOrders = findViewById(R.id.card_manage_order);

        // Set onClick Listeners for each card
        cardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Manage Products Activity
                Intent intent = new Intent(AdminActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            }
        });

        cardManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Manage Categories Activity
                Intent intent = new Intent(AdminActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
//
//        cardManageUsers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Navigate to Manage Users Activity
//                Intent intent = new Intent(AdminActivity.this, ManageUsersActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        cardManageOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Navigate to Manage Orders Activity
//                Intent intent = new Intent(AdminActivity.this, ManageOrdersActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
