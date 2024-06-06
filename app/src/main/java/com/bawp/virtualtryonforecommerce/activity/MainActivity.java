package com.bawp.virtualtryonforecommerce.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseInitializer.initialize(this);
//        // After initialization, get the Firestore instance
//        db = FirebaseFirestore.getInstance();
//
//        // Your existing code to add a user to Firestore
//        Map<String, Object> users = new HashMap<>();
//        users.put("name", "Alex Smith");
//        users.put("Location", "UK");
//
//        db.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d("TAG", "onSuccess: Successfully added.");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG", "onFailure: Failed to add.", e);
//            }
//        });
    }
}
