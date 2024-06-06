package com.bawp.virtualtryonforecommerce.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.virtualtryonforecommerce.ClickListener.OnProductClickListener;
import com.bawp.virtualtryonforecommerce.R;
import com.bawp.virtualtryonforecommerce.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view, onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        // Bind product data to ViewHolder
        holder.bind(product);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productNameTextView, productPrice;
        RelativeLayout productLayout;

        ViewHolder(@NonNull View itemView, final OnProductClickListener onProductClickListener) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productLayout = itemView.findViewById(R.id.productLayout);

            productLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onProductClickListener.onProductClick(position);
                    }
                }
            });

        }

        void bind(Product product) {
            productNameTextView.setText(product.getProductName());
            productPrice.setText(String.valueOf(product.getProductPrice()));

            if (!product.getProductImages().isEmpty()) {
                // Load image using Glide
                Glide.with(itemView)
                        .load(product.getProductImages().get(0)) // Assuming getProductImageURL() returns the URL of the image
                        .placeholder(R.drawable.image) // Optional placeholder image while loading
                        .error(R.drawable.image) // Optional error image if the loading fails
                        .into(productImage);
            }
            else {
                productImage.setImageResource(R.drawable.image);
            }
        }
    }
}

