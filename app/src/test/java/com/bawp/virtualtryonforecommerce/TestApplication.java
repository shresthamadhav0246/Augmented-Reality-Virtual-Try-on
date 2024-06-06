package com.bawp.virtualtryonforecommerce;

import android.app.Application;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Set up your theme or other global settings here
        setTheme(R.style.Theme_VirtualTryOnForEcommerce); // Replace with your actual theme that extends from Theme.AppCompat
    }
}

