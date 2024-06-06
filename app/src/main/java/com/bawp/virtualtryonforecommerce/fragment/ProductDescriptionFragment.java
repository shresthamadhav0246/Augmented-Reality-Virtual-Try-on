package com.bawp.virtualtryonforecommerce.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bawp.virtualtryonforecommerce.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProductDescriptionFragment extends BottomSheetDialogFragment {
    private String productDescription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style for the dialog fragment
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);

        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(productDescription);

        // Post a runnable to get the root view after it has been attached to the parent
        view.post(new Runnable() {
            @Override
            public void run() {
                // Get the root view of the fragment's layout
                View rootView = getView();

                if (rootView != null) {
                    // Check if the root view is a child of CoordinatorLayout
                    if (rootView.getParent() instanceof CoordinatorLayout) {
                        // Customize behavior to expand upwards when scrolling up
                        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(rootView);
                        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                            @Override
                            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                // Not used
                            }

                            @Override
                            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                                // Expand upwards when sliding up
                                if (slideOffset < 0) {
                                    behavior.setPeekHeight((int) (bottomSheet.getHeight() * 0.5));
                                } else {
                                    behavior.setPeekHeight((int) (bottomSheet.getHeight() * 0.1));
                                }
                            }
                        });
                    } else {
                        Log.e("TAG", "Root view is not a child of CoordinatorLayout");
                    }
                }
            }
        });

        return view;
    }




    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
