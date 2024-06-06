package com.bawp.virtualtryonforecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.SunglassesCategoryAdapter;
import com.bawp.virtualtryonforecommerce.model.Product;
import java.util.ArrayList;
import java.util.List;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.model.Sunglasses;

public class SunglassesCategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SunglassesCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunglasses_category);

        recyclerView = findViewById(R.id.sunglassesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Sunglasses> products = new ArrayList<>();
        products.add(new Sunglasses("Aviator", 150, "models/glass3.png"));
        products.add(new Sunglasses("Wayfarer", 130, "models/glass19.png"));
        products.add(new Sunglasses("Round Metal", 180, "models/glass10.png"));
        products.add(new Sunglasses("Black Sunglasses", 120, "models/glass8.png"));

        adapter = new SunglassesCategoryAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }
}
