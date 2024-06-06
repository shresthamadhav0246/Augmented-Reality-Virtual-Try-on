package com.bawp.virtualtryonforecommerce.activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.RecyclerViewAdapter.SunglassesAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFrontFacingFragment;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SunglassesVirtualTryOn extends AppCompatActivity implements SunglassesAdapter.SunglassesClickListener {

    private final Set<CompletableFuture<?>> loaders = new HashSet<>();

    private ArFrontFacingFragment arFragment;
    private ArSceneView arSceneView;
    private Texture faceTexture;
    private ModelRenderable faceModel;
    private final HashMap<AugmentedFace, AugmentedFaceNode> facesNodes = new HashMap<>();

    private Texture[] sunglassesTextures; // Array to hold textures of sunglasses
    private RecyclerView recyclerViewSunglasses;
    private SunglassesAdapter sunglassesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunglasses_virtual_try_on);

        sunglassesTextures = new Texture[4];

        getSupportFragmentManager().addFragmentOnAttachListener(this::onAttachFragment);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFrontFacingFragment.class, null)
                        .commit();
            }
        }

        recyclerViewSunglasses = findViewById(R.id.recyclerViewSunglasses);
        recyclerViewSunglasses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        loadTextures("models/glass3.png");

        // Populate RecyclerView with sunglasses images
        List<String> sunglassesImages = new ArrayList<>();
        for (int i = 0; i < sunglassesTextures.length; i++) {
            sunglassesImages.add("models/glass3.png");
            sunglassesImages.add("models/glass19.png");
            sunglassesImages.add("models/glass10.png");
            sunglassesImages.add("models/glass8.png");// Replace with your sunglasses images
        }
        sunglassesAdapter = new SunglassesAdapter(this, sunglassesImages, this);
        recyclerViewSunglasses.setAdapter(sunglassesAdapter);

        ImageView crossIcon = findViewById(R.id.crossIcon);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Destroy AR session
                destroyARSession();
            }
        });

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



    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFrontFacingFragment) fragment;
            arFragment.setOnViewCreatedListener(this::onViewCreated);
        }
    }

    public void onViewCreated(ArSceneView arSceneView) {
        this.arSceneView = arSceneView;
        // Show instruction message for tapping to place product for 3 seconds



        // This is important to make sure that the camera stream renders first so that
        // the face mesh occlusion works correctly.
        arSceneView.setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        // Check for face detections
        arFragment.setOnAugmentedFaceUpdateListener(this::onAugmentedFaceTrackingUpdate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (CompletableFuture<?> loader : loaders) {
            if (!loader.isDone()) {
                loader.cancel(true);
            }
        }
    }


    private void loadTextures(String sunglassesPath) {


//        String[] sunglassesPaths = {"models/glass3.png", "models/glass19.png", "models/glass10.png", "models/glass8.png"};
//        sunglassesTextures = new Texture[sunglassesPaths.length];
//
//        for (int i = 0; i < sunglassesPaths.length; i++) {
//            final int index = i;

        loaders.add(Texture.builder()
                .setSource(this, Uri.parse(sunglassesPath))
                .setUsage(Texture.Usage.COLOR_MAP)
                .build()
                .thenAccept(texture -> {
                    faceTexture = texture;

                    // Update the face nodes with the new texture
                    for (AugmentedFaceNode faceNode : facesNodes.values()) {
                        faceNode.setFaceMeshTexture(faceTexture);
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load texture", Toast.LENGTH_LONG).show();
                    return null;
                }));
    }

//}

    public void onAugmentedFaceTrackingUpdate(AugmentedFace augmentedFace) {
        if (faceTexture == null) {
            return;
        }

        AugmentedFaceNode existingFaceNode = facesNodes.get(augmentedFace);

        switch (augmentedFace.getTrackingState()) {
            case TRACKING:
                if (existingFaceNode == null) {
                    AugmentedFaceNode faceNode = new AugmentedFaceNode(augmentedFace);


                    faceNode.setFaceMeshTexture(faceTexture);

                    float offsetY = 0.05f; // Adjust up or down
                    float scaleX = 1f; // Adjust horizontal scale
                    float scaleY = 0.4f; // Adjust vertical scale

                    // Apply the position and scale adjustments
                    Vector3 adjustedPosition = new Vector3(0.0f, offsetY, 0.0f);
                    faceNode.setLocalPosition(adjustedPosition);
                    faceNode.setLocalScale(new Vector3(scaleX, scaleY, 1.0f)); // Keep Z scale to 1 to avoid stretching


                    arSceneView.getScene().addChild(faceNode);

                    facesNodes.put(augmentedFace, faceNode);
                }
                break;
            case STOPPED:
                if (existingFaceNode != null) {
                    arSceneView.getScene().removeChild(existingFaceNode);
                }
                facesNodes.remove(augmentedFace);
                break;
        }
    }

    @Override
    public void onSunglassesClick(String sunglassesImageUrl) {

        loadTextures(sunglassesImageUrl);
//        // Load the texture for the selected sunglasses
//        Texture selectedSunglassesTexture = loadTextureFromAssets(sunglassesImageUrl);
//
//        // Update the face texture with the selected sunglasses texture
//        if (selectedSunglassesTexture != null) {
//            faceTexture = selectedSunglassesTexture;
//
//            // Update the face nodes with the new texture
//            for (AugmentedFaceNode faceNode : facesNodes.values()) {
//                faceNode.setFaceMeshTexture(faceTexture);
//            }
//        } else {
//            Toast.makeText(this, "Failed to load sunglasses texture", Toast.LENGTH_SHORT).show();
//        }
    }

    private Texture loadTextureFromAssets(String sunglassesImageUrl) {
        try {
            // Load texture from assets
            return Texture.builder()
                    .setSource(this, Uri.parse(sunglassesImageUrl))
                    .setUsage(Texture.Usage.COLOR_MAP)
                    .build()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}