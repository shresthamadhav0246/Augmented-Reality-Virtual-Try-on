package com.bawp.virtualtryonforecommerce.database;


import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInitializer {

    public static void initialize(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("virtual-try-on-bc3eb")
                .setApplicationId("1:327247816133:android:2e0a6b4b521bae5d0c97ac") // Your App ID
                .setApiKey("AIzaSyB8iJk-doyCFsnEqGyIqsPeA5l8WJcnPIQ") // Your API key
                .setDatabaseUrl("https://virtual-try-on-bc3eb-default-rtdb.firebaseio.com") // Your Database URL
                .setStorageBucket("virtual-try-on-bc3eb.appspot.com") // Your Storage Bucket
                .build();

        // Check if default FirebaseApp is already initialized
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context, options, FirebaseApp.DEFAULT_APP_NAME);
        }
    }
}
