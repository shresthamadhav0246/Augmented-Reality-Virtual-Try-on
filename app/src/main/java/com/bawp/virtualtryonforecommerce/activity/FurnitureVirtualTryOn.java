package com.bawp.virtualtryonforecommerce.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.database.FirebaseInitializer;
import com.bawp.virtualtryonforecommerce.fragment.ProductDescriptionFragment;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

public class FurnitureVirtualTryOn extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {

    public ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;
    public String modelUri;
    private String productDescription;
    private Double price;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_furniture_virtual_try_on);
        getSupportFragmentManager().addFragmentOnAttachListener(this);
        ImageView btnCaptureImage = findViewById(R.id.captureImage);


        FirebaseInitializer.initialize(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("modelUri")) {
            modelUri = intent.getStringExtra("modelUri");
            productDescription = intent.getStringExtra("productDescription");
            title = intent.getStringExtra("title");
            price = intent.getDoubleExtra("price", 0);
        }

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }

        loadModels();

        btnCaptureImage.setOnClickListener(view -> {
            captureAndShareImage();
        });

        ImageView descriptionIcon = findViewById(R.id.descriptionIcon);
        descriptionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show modal dialog for description
                showDescriptionModal();
            }
        });

        ImageView crossIcon = findViewById(R.id.crossIcon);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Destroy AR session
                destroyARSession();
            }
        });

        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the checkout page
                navigateToCheckoutPage();
            }
        });

    }

    private void showDescriptionModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Description");
        builder.setMessage(productDescription);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void destroyARSession() {
        if (arFragment != null && arFragment.getArSceneView() != null) {
            arFragment.getArSceneView().getSession().close();
            Toast.makeText(this, "AR Session Closed", Toast.LENGTH_SHORT).show();
            // Finish the activity to return to the product description page
            finish();
        } else {
            Toast.makeText(this, "AR Session not initialized", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToCheckoutPage() {
        Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
        checkoutIntent.putExtra("title", title);
        checkoutIntent.putExtra("price", price);
        startActivity(checkoutIntent);
    }


    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
    }

    @Override
    public void onViewCreated(ArSceneView arSceneView) {
        arFragment.setOnViewCreatedListener(null);

        // Fine adjust the maximum frame rate
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
    }

    public void loadModels() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReferenceFromUrl(modelUri);



        WeakReference<FurnitureVirtualTryOn> weakActivity = new WeakReference<>(this);
        modelRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(bytes -> {
                    try {
                        // Create a temporary file in the app's internal storage
                        File file = File.createTempFile("model", "glb", getApplicationContext().getCacheDir());
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            fos.write(bytes);
                        }

                        // Once the file is written, create a ModelRenderable from the URI of the temporary file
                        Uri fileUri = Uri.fromFile(file);
                        ModelRenderable.builder()
                                .setSource(this, fileUri)
                                .setIsFilamentGltf(true)
                                .setAsyncLoadEnabled(true)
                                .build()
                                .thenAccept(model -> {
                                    FurnitureVirtualTryOn activity = weakActivity.get();
                                    if (activity != null) {
                                        activity.model = model;
                                        // Model is loaded, proceed with your logic
                                    }
                                })
                                .exceptionally(throwable -> {
                                    // Handle any errors
                                    Toast.makeText(this, "Unable to load 3D model", Toast.LENGTH_LONG).show();
                                    return null;
                                });
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to create temporary file", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors
                    Toast.makeText(this, "Failed to download model: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                });

        ViewRenderable.builder()
                .setView(this, R.layout.view_model_title)
                .build()
                .thenAccept(viewRenderable -> {
                    FurnitureVirtualTryOn activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                        TextView textView = (TextView) viewRenderable.getView().findViewById(R.id.model_title);
                        textView.setText(title);
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }


    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (model == null || viewRenderable == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create the transformable model and add it to the anchor.
        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
        model.setParent(anchorNode);
        model.setRenderable(this.model)
                .animate(true).start();
        model.select();

        Node titleNode = new Node();
        titleNode.setParent(model);
        titleNode.setEnabled(false);
        titleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));
        titleNode.setRenderable(viewRenderable);
        titleNode.setEnabled(true);
    }

    public void captureAndShareImage() {
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    // Save the image to a file in the cache directory.
                    File file = new File(getExternalCacheDir(), "ARScene.jpg");
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }

                    // Share the image.
                    Uri photoURI = FileProvider.getUriForFile(FurnitureVirtualTryOn.this,
                            getApplicationContext().getPackageName() + ".provider", file);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share AR Scene"));

                } catch (IOException e) {
                    Toast.makeText(FurnitureVirtualTryOn.this, e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(FurnitureVirtualTryOn.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG).show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


}